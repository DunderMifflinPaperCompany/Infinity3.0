package com.dundermifflin

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Unroll

@SpringBootTest(classes = Application, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationSpec extends HttpSpec {
    void 'homepage preserves content and renders GSP with chat enabled'() {
        when:
        def response = get('/')
        String html = response.body()

        then:
        response.statusCode() == 200
        response.headers().firstValue('content-type').get().contains('text/html')
        html.contains('<title>Dunder Mifflin Paper Company - Infinity 2.0</title>')
        ['Michael Scott', 'Jim Halpert', 'Dwight K. Schrute', 'Pam Beesly',
         'Infinity 2.0 Launch: Revolutionary Upgrade!', 'Q4 Sales Records Broken Again!',
         'New Mobile App Available', '400%', '73%', '∞',
         'Scranton, PA', 'Stamford, CT', 'Utica, NY', 'Nashua, NH', 'Buffalo, NY'].every {
            html.contains(it)
        }
        html.count('class="employee-card"') == 4
        html.count('class="news-item"') == 3
        html.contains('id="chatContainer"')
        html.contains("fetch('/api/chat/start'")
        html.contains("fetch('/api/chat/end/' + currentSessionId")
        !html.contains('{{')
        !html.contains('{%')
        !html.contains('<g:')
        !html.contains('${')
    }

    void 'stylesheet remains available at its original URL'() {
        when:
        def response = get('/static/css/style.css')

        then:
        response.statusCode() == 200
        response.headers().firstValue('content-type').get().contains('text/css')
        response.body() == getClass().getResource('/static/css/style.css').getText('UTF-8')
    }

    void 'offices retain their original JSON schema and order'() {
        when:
        def response = get('/api/offices')
        def offices = json(response)

        then:
        response.statusCode() == 200
        offices*.id == ['scranton', 'stamford', 'utica', 'nashua', 'buffalo']
        offices*.name == ['Scranton', 'Stamford', 'Utica', 'Nashua', 'Buffalo']
        offices*.location == ['Scranton, PA', 'Stamford, CT', 'Utica, NY', 'Nashua, NH', 'Buffalo, NY']
        offices[0].available_salespeople == ['michael_scott', 'jim_halpert', 'dwight_schrute']
        offices.every { it.keySet() == ['id', 'name', 'location', 'available_salespeople'] as Set }
    }

    @Unroll
    void 'starting and ending a chat at #office preserves #status behavior'() {
        when:
        def response = post('/api/chat/start', [office_id: office, customer_name: 'Customer'])
        def result = json(response)

        then:
        response.statusCode() == 200
        result.keySet() == ['session_id', 'status', 'message', 'salesperson'] as Set
        UUID.fromString(result.session_id).toString() == result.session_id
        result.status == status
        result.message == message
        result.salesperson?.id == salesperson
        result.salesperson == null || (
            result.salesperson.office_id == office &&
            result.salesperson.is_available == true &&
            result.salesperson.keySet() ==
                ['id', 'name', 'title', 'office_id', 'is_available', 'quote'] as Set)

        when:
        def ended = post("/api/chat/end/${result.session_id}", null)

        then:
        ended.statusCode() == 200
        json(ended) == 'Chat ended successfully'
        post("/api/chat/end/${result.session_id}", null).statusCode() == 200

        where:
        office     | status      | salesperson     | message
        'scranton' | 'connected' | 'michael_scott'  | 'Connected to Michael Scott from Scranton'
        'stamford' | 'connected' | 'andy_bernard'   | 'Connected to Andy Bernard from Stamford'
        'nashua'   | 'connected' | 'holly_flax'     | 'Connected to Holly Flax from Nashua'
        'utica'    | 'waiting'   | null            | 'All Utica representatives are currently busy. You are in queue.'
        'buffalo'  | 'waiting'   | null            | 'All Buffalo representatives are currently busy. You are in queue.'
    }

    void 'customer name is optional or nullable and sessions get unique IDs'() {
        when:
        def first = post('/api/chat/start', [office_id: 'scranton'])
        def second = post('/api/chat/start', [office_id: 'scranton', customer_name: null])

        then:
        first.statusCode() == 200
        second.statusCode() == 200
        json(first).session_id != json(second).session_id
    }

    @Unroll
    void 'invalid chat payload #payload returns 400'() {
        expect:
        post('/api/chat/start', payload).statusCode() == 400

        where:
        payload << [
            [:], [office_id: null], [office_id: ''], [office_id: 'unknown'],
            [office_id: 123], [office_id: ['scranton']],
            [office_id: 'scranton', customer_name: 123],
            [office_id: 'scranton', customer_name: [:]],
            [], 'scranton', null
        ]
    }

    void 'malformed JSON and non-JSON requests fail explicitly'() {
        expect:
        send('POST', '/api/chat/start', '{bad json', 'application/json').statusCode() == 400
        send('POST', '/api/chat/start', '', 'application/json').statusCode() == 400
        send('POST', '/api/chat/start', 'office_id=scranton',
            'application/x-www-form-urlencoded').statusCode() == 415
        send('POST', '/api/chat/start', '{"office_id":"scranton"}',
            'application/jsonp').statusCode() == 415
        send('POST', '/api/chat/start', '{"office_id":"scranton"}',
            'application/json; charset=UTF-8').statusCode() == 200
        send('POST', '/api/chat/start', '{"office_id":"scranton"}',
            'application/vnd.infinity+json').statusCode() == 200
    }

    @Unroll
    void 'ending an invalid or unknown session #id returns #status'() {
        expect:
        post("/api/chat/end/$id", null).statusCode() == status

        where:
        id                                      | status
        'not-a-uuid'                             | 400
        '1-1-1-1-1'                             | 400
        '00000000-0000-0000-0000-000000000000'  | 404
    }

    void 'routes reject unsupported methods and unknown paths'() {
        expect:
        get('/api/chat/start').statusCode() == 405
        get("/api/chat/end/${UUID.randomUUID()}").statusCode() == 405
        post('/api/offices', [:]).statusCode() == 405
        post('/', [:]).statusCode() == 405
        get('/missing').statusCode() == 404
        get('/static/css/missing.css').statusCode() == 404
    }
}
