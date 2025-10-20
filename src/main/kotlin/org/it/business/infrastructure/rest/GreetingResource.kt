package org.it.business.infrastructure.rest

import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.it.business.domain.entity.MessageBundle
import org.it.business.domain.entity.MessageBundleId
import org.it.business.domain.repository.MessageBundleRepository
import org.it.business.domain.valueobject.Locale

/**
 * REST adapter for greeting functionality
 * This is a driving adapter in the hexagonal architecture
 */
@Path("/hello")
class GreetingResource @Inject constructor(
    private val messageBundleRepository: MessageBundleRepository
) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        // Simple greeting without i18n for now
        return "Hello from Quarkus REST with Hexagonal Architecture!"
    }
    
    @GET
    @Path("/i18n")
    @Produces(MediaType.APPLICATION_JSON)
    fun helloWithI18n(): Map<String, String> {
        // Example of using the message bundle repository directly
        val englishBundle = messageBundleRepository.findByLocale(Locale.ENGLISH)
        val welcomeMessage = englishBundle?.getMessage(org.it.business.domain.valueobject.MessageKey.WELCOME) 
            ?: "Welcome!"
            
        return mapOf(
            "message" to welcomeMessage,
            "architecture" to "hexagonal"
        )
    }
}
