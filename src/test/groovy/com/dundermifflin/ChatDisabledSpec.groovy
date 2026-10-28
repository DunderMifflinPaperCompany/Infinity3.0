package com.dundermifflin

import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = Application, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = 'chat.enabled=false')
class ChatDisabledSpec extends HttpSpec {
    void 'disabled chat hides the widget and rejects all chat APIs'() {
        when:
        def response = get('/')

        then:
        response.statusCode() == 200
        !response.body().contains('id="chatContainer"')
        !response.body().contains("fetch('/api/chat/start'")
        response.body().contains('Michael Scott')
        get('/api/offices').statusCode() == 503
        post('/api/chat/start', [office_id: 'scranton']).statusCode() == 503
        post("/api/chat/end/${UUID.randomUUID()}", null).statusCode() == 503
    }
}
