package com.dundermifflin

import grails.converters.JSON
import org.grails.web.converters.exceptions.ConverterException

class ChatController {
    static allowedMethods = [offices: 'GET', start: 'POST', end: 'POST']

    ChatService chatService

    def offices() {
        render chatService.offices as JSON
    }

    def start() {
        String mimeType = request.contentType?.split(';', 2)?.getAt(0)?.trim()?.toLowerCase(Locale.ROOT)
        if (mimeType != 'application/json' &&
            !(mimeType?.startsWith('application/') && mimeType.endsWith('+json'))) {
            render status: 415
            return
        }

        def payload
        try {
            payload = request.JSON
        } catch (ConverterException exception) {
            log.debug('Invalid chat JSON request', exception)
            render status: 400
            return
        }

        if (!(payload instanceof Map) || !(payload.office_id instanceof String) ||
            (payload.customer_name != null && !(payload.customer_name instanceof String))) {
            render status: 400
            return
        }

        try {
            render chatService.start(payload.office_id, payload.customer_name) as JSON
        } catch (IllegalArgumentException exception) {
            log.debug('Invalid chat office', exception)
            render status: 400
        }
    }

    def end(String sessionId) {
        UUID id
        try {
            id = UUID.fromString(sessionId)
            if (id.toString() != sessionId.toLowerCase(Locale.ROOT)) {
                throw new IllegalArgumentException('Invalid UUID')
            }
        } catch (IllegalArgumentException exception) {
            log.debug('Invalid chat session ID', exception)
            render status: 400
            return
        }

        if (chatService.end(id)) {
            render text: '"Chat ended successfully"', contentType: 'application/json'
        } else {
            render status: 404
        }
    }
}
