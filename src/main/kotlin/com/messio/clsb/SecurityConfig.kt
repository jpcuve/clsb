package com.messio.clsb

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.Exception
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    @Value("\${app.client-id}") val clientId: String,
    @Value("\${app.issuer}") val issuer: String,
    @Value("\${app.public-key}") val publicKeyAsString: String,
    @Value("\${app.asymmetric-cipher}") val asymmetricCipher: String,
) {
    private val keyFactory: KeyFactory = KeyFactory.getInstance(asymmetricCipher)
    private val publicKey = keyFactory.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyAsString))) as RSAPublicKey
    private val algorithm = Algorithm.RSA256(publicKey, null)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
            .addFilterAfter(securityFilter(), BasicAuthenticationFilter::class.java)
            .csrf().disable() // not for production
            .headers().frameOptions().disable().and() // not for production, necessary for H2 console
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()
        return http.build()
    }

    @Bean
    fun securityFilter() = object: OncePerRequestFilter() {
        override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
            logger.debug("URI: ${req.method} ${req.requestURI}")
            SecurityContextHolder.getContext().authentication = null
            req.getHeader("Authorization")?.let { authorization ->
                if (authorization.lowercase().startsWith("bearer ")){
                    val idToken = authorization.substring(7).trim()
                    try {
                        val decodedIdToken = JWT.require(algorithm).withIssuer(issuer).build().verify(idToken)
                        if (decodedIdToken.audience.contains(clientId)) {
                            val email = decodedIdToken.getClaim("email").asString()
                            val roles = decodedIdToken.getClaim("roles").asString()
                            logger.debug("Authenticated: ${req.requestURI} $email ($roles)")
                            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                                email,
                                idToken,  // the token is available in the password field
                                roles.split("\\s+".toRegex()).map { SimpleGrantedAuthority("ROLE_${it.uppercase()}") }
                            )
                        }
                    } catch (e: Exception){
                        logger.error(e.message, e)
                    }
                }
            }
            chain.doFilter(req, res)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)
    }
}
