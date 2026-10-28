package com.dundermifflin

class HomeController {
    static allowedMethods = [index: 'GET']

    ChatService chatService

    def index() {
        boolean chatEnabled = grailsApplication.config.getProperty('chat.enabled', Boolean, true)
        render view: '/index', model: [
            employees: CompanyData.EMPLOYEES,
            news: CompanyData.NEWS,
            version: '2.0',
            company: 'Dunder Mifflin Paper Company',
            chat_enabled: chatEnabled,
            offices: chatEnabled ? chatService.offices : []
        ]
    }
}
