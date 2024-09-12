package com.messio.clsb

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.io.Resource
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
class WebSocketConfig(
    @Value("\${app.allowed-origins}") val allowedOrigins: String,
): WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // must be specific allowed origin, not wildcard
        val endpoint = registry.addEndpoint("/messaging")
        if (allowedOrigins.isNotBlank()){
            endpoint.setAllowedOrigins(*allowedOrigins.split(",").toTypedArray())
        }
        endpoint.withSockJS()
    }
}
