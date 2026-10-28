package com.dundermifflin

class UrlMappings {
    static mappings = {
        '/'(controller: 'home', action: 'index')
        '/api/offices'(controller: 'chat', action: 'offices')
        '/api/chat/start'(controller: 'chat', action: 'start')
        "/api/chat/end/$sessionId"(controller: 'chat', action: 'end')
    }
}
