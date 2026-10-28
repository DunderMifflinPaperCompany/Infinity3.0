package com.dundermifflin

class ChatInterceptor {
    ChatInterceptor() {
        match(controller: 'chat')
    }

    boolean before() {
        if (!grailsApplication.config.getProperty('chat.enabled', Boolean, true)) {
            render status: 503
            return false
        }
        true
    }
}
