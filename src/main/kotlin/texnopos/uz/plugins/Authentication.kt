package texnopos.uz.plugins

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import texnopos.uz.utils.TokenManager

fun Application.configureAuthentication() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)
    install(Authentication) {
        jwt {
            verifier(tokenManager.verifyJWT())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("username").asString()
                        .isNotEmpty()
                ) JWTPrincipal(jwtCredential.payload) else null
            }
        }
    }
}