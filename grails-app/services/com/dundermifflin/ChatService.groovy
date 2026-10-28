package com.dundermifflin

import groovy.json.JsonOutput
import java.time.Instant

class ChatService {
    private final Map<UUID, Map<String, Object>> sessions = [:]

    List<Map<String, Object>> getOffices() {
        CompanyData.OFFICES
    }

    synchronized Map<String, Object> start(String officeId, String customerName) {
        Map office = offices.find { it.id == officeId }
        if (!office) {
            throw new IllegalArgumentException("Unknown office: $officeId")
        }

        Map salesperson = CompanyData.SALESPEOPLE.find {
            it.office_id == officeId && it.is_available
        }
        UUID sessionId = UUID.randomUUID()
        sessions[sessionId] = [
            id: sessionId.toString(),
            customer_id: customerName,
            office_id: officeId,
            salesperson_id: salesperson?.id,
            status: salesperson ? 'Connected' : 'Waiting',
            started_at: Instant.now().toString(),
            ended_at: null,
            messages: []
        ]
        logEvent('chat_started', sessions[sessionId], "Chat started for office: ${office.name}")

        [
            session_id: sessionId.toString(),
            status: salesperson ? 'connected' : 'waiting',
            message: salesperson ?
                "Connected to ${salesperson.name} from ${office.name}".toString() :
                "All ${office.name} representatives are currently busy. You are in queue.".toString(),
            salesperson: salesperson
        ]
    }

    synchronized boolean end(UUID sessionId) {
        Map session = sessions[sessionId]
        if (!session) {
            return false
        }
        session.status = 'Ended'
        session.ended_at = Instant.now().toString()
        logEvent('chat_ended', session, 'Chat ended by user')
        true
    }

    private void logEvent(String eventType, Map session, String details) {
        log.info('CHAT_EVENT: {}', JsonOutput.toJson([
            event_type: eventType,
            session_id: session.id,
            office_id: session.office_id,
            salesperson_id: session.salesperson_id,
            timestamp: Instant.now().toString(),
            details: details
        ]))
    }
}
