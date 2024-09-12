package com.messio.clsb

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
class WebSocketConfig(
    val env: Environment,
): WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        // must be specific allowed origin, not wildcard
        val endpoint = registry.addEndpoint("/messaging")
        if (env.activeProfiles.isEmpty()){
            endpoint.setAllowedOrigins("http://localhost:5173")
        }
        endpoint.withSockJS()
    }
}
