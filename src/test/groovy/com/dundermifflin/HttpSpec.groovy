package com.dundermifflin

import grails.boot.test.GrailsApplicationContextLoader
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

@ContextConfiguration(loader = GrailsApplicationContextLoader)
abstract class HttpSpec extends Specification {
    @Value('${local.server.port}')
    int port

    HttpResponse<String> get(String path) {
        send('GET', path)
    }

    HttpResponse<String> post(String path, Object body) {
        send('POST', path, JsonOutput.toJson(body), 'application/json')
    }

    HttpResponse<String> send(String method, String path, String body = '', String contentType = null) {
        def builder = HttpRequest.newBuilder(URI.create("http://127.0.0.1:$port$path"))
            .method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
        if (contentType) {
            builder.header('Content-Type', contentType)
        }
        HttpClient.newHttpClient().send(builder.build(),
            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
    }

    Object json(HttpResponse<String> response) {
        new JsonSlurper().parseText(response.body())
    }
}
