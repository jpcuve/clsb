package com.messio.clsb

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.net.http.HttpResponse

@Configuration
class JsonConfig {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
//        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(JavaTimeModule())
    }

    @Bean
    fun objectWriter(objectMapper: ObjectMapper): ObjectWriter = objectMapper.writer()

    @Bean
    fun objectReader(objectMapper: ObjectMapper): ObjectReader = objectMapper.reader()

    @Bean
    fun jsonBodyHandler(objectReader: ObjectReader) = HttpResponse.BodyHandler {
        HttpResponse.BodySubscribers.mapping(HttpResponse.BodySubscribers.ofByteArray()) {
            objectReader.readTree(ByteArrayInputStream(it))
        }
    }
}
