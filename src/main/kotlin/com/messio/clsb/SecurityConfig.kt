package com.messio.clsb

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.io.ByteArrayInputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    val jsonNodeBodyHandler: HttpResponse.BodyHandler<JsonNode>,
    @Value("\${app.identity.client-id}") val clientId: String,
    @Value("\${app.allowed-origins}") val allowedOriginsAsString: String,
    @Value("\${app.identity.jwk-set-url}") val jwkSetUrl: String,
) {

    @Bean
    fun keyPair(): KeyPair {
        if (jwkSetUrl.startsWith("http")) {
            val httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2L)).build()
            val req = HttpRequest.newBuilder().GET().uri(URI.create(jwkSetUrl)).build()
            var delay = 1000L
            while (delay < 120000L) {
                try {
                    val res = httpClient.send(req, jsonNodeBodyHandler)
                    val key0 = res.body()["keys"][0] as ObjectNode
                    val certificateChain = key0.get("x5c") as ArrayNode
                    val decodedCertificate = Base64.getDecoder().decode(certificateChain[0].asText())
                    ByteArrayInputStream(decodedCertificate).use { inputStream ->
                        val certificateFactory = CertificateFactory.getInstance("X.509")
                        val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
                        val publicKey = certificate.publicKey as RSAPublicKey
                        return KeyPair(publicKey, null)
                    }
                } catch (_: Exception) {
                    // ignore
                }
                logger.info("Waiting for public key... {}ms", delay)
                Thread.sleep(delay)
                delay = delay / 2 * 3
            }
        }
        return KeyPairGenerator.getInstance("RSA").genKeyPair()
    }

    @Bean
    fun jwtVerifier(keyPair: KeyPair): JWTVerifier {
        val algorithm = Algorithm.RSA256(keyPair.public as RSAPublicKey, keyPair.private as RSAPrivateKey?)
        return JWT.require(algorithm).build()
    }

    @Bean
    @Order(1)
    fun apiFilterChain(http: HttpSecurity, verifier: JWTVerifier): SecurityFilterChain {
        http
            .securityMatcher("/api/**")
            .csrf {
                it.disable()
            }
            .cors {
                it.configurationSource(UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues().apply {
                        allowedOrigins = allowedOriginsAsString.split(",")
                    })
                })
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .headers {
                it.frameOptions { it2 ->
                    it2.disable()
                }
                it.httpStrictTransportSecurity { it2 ->
                    it2.disable()
                }
            }
            .addFilterAfter(securityFilter(verifier), BasicAuthenticationFilter::class.java)
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
        return http.build()
    }

    @Bean
    @Order(2)
    fun authFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/auth/**")
            .csrf {
                it.disable()
            }
            .cors { // always allow all
                it.configurationSource(UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues().apply { addAllowedMethod("DELETE") })
                })
            }
            .sessionManagement() {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
        return http.build()
    }

    @Bean
    @Order(3)
    fun h2ConsoleFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(PathRequest.toH2Console())
            .csrf {
                it.disable()
            }
            .headers { headers ->
                headers.frameOptions {
                    it.sameOrigin()
                }
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
        return http.build()
    }

    @Bean
    fun webFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher(AnyRequestMatcher.INSTANCE)
            .csrf {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
            }
        return http.build()
    }

    @Bean
    fun securityFilter(verifier: JWTVerifier) = object: OncePerRequestFilter() {
        override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
            SecurityContextHolder.getContext().authentication = null
            var email = ""
            val idTokens = mutableListOf<String?>()
            idTokens.add(req.getBearerToken())
            idTokens.add(req.getParameter("id-token"))
            idTokens.firstNotNullOfOrNull {it}?.let { idToken ->
                try {
                    val decodedIdToken = verifier.verify(idToken)
                    val accountId = decodedIdToken.subject.toLong()
                    email = decodedIdToken.getClaim("email").asString()
                    val roles = decodedIdToken.getClaim("roles").asArray(String::class.java)
                    val features = decodedIdToken.getClaim("features").asArray(String::class.java)
                    val aspects = decodedIdToken.getClaim("aspects").asArray(String::class.java)
                    if (decodedIdToken.audience.contains(clientId)) {
                        SecurityContextHolder.getContext().authentication = GateAuthenticationToken(
                            accountId,
                            idToken,
                            email,
                            roles,
                            features,
                            aspects,
                        )
                    } else {
                        logger.error("Client id not found in audience (${decodedIdToken.audience.joinToString(" ")}): $clientId")
                    }
                } catch (e: Exception){
                    logger.error(e.message, e)
                }

            }
            logger.debug("URI: ${req.method} ${req.requestURI} - ${email}")
            chain.doFilter(req, res)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}

