package com.dundermifflin

import spock.lang.Specification
import java.time.Instant
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class ChatServiceSpec extends Specification {
    void 'session lifecycle retains customer, timestamps, status and messages'() {
        given:
        def service = new ChatService()

        when:
        def started = service.start('scranton', 'Customer')
        UUID id = UUID.fromString(started.session_id)
        Map session = service.@sessions[id]

        then:
        session.customer_id == 'Customer'
        session.office_id == 'scranton'
        session.salesperson_id == 'michael_scott'
        session.status == 'Connected'
        Instant.parse(session.started_at)
        session.ended_at == null
        session.messages == []

        when:
        boolean ended = service.end(id)

        then:
        ended
        session.status == 'Ended'
        !Instant.parse(session.ended_at).isBefore(Instant.parse(session.started_at))
        !service.end(UUID.randomUUID())
    }

    void 'concurrent starts and ends preserve independent sessions'() {
        given:
        def service = new ChatService()
        def executor = Executors.newFixedThreadPool(4)

        when:
        List<Callable<String>> tasks = (1..24).collect {
            { ->
                String id = service.start('buffalo', null).session_id
                assert service.end(UUID.fromString(id))
                id
            } as Callable<String>
        }
        def ids = executor.invokeAll(tasks)*.get()

        then:
        ids.toSet().size() == 24
        service.@sessions.size() == 24
        service.@sessions.values().every { it.status == 'Ended' && it.ended_at != null }

        cleanup:
        executor.shutdownNow()
    }
}
