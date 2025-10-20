package org.it.business.infrastructure.rest

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.it.business.application.usecase.DetectLocaleUseCase
import org.it.business.application.usecase.GetAllMessagesUseCase
import org.it.business.application.usecase.GetMessageUseCase
import org.it.business.application.usecase.GetMessageRequest
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey

/**
 * REST adapter for i18n functionality
 * This is a driving adapter in the hexagonal architecture
 */
@Path("/api/i18n")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class I18nResource @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase,
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val detectLocaleUseCase: DetectLocaleUseCase
) {

    @GET
    @Path("/welcome")
    fun getWelcomeMessage(
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getMessageUseCase.execute(GetMessageRequest.Welcome(locale))
        
        return if (response.found) {
            Response.ok(mapOf(
                "message" to response.message!!.resolve(),
                "locale" to response.message.locale.language
            )).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Message not found"))
                .build()
        }
    }

    @GET
    @Path("/greeting")
    fun getGreeting(
        @QueryParam("name") name: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getMessageUseCase.execute(GetMessageRequest.Greeting(name, locale))
        
        return if (response.found) {
            Response.ok(mapOf(
                "message" to response.message!!.resolve(),
                "locale" to response.message.locale.language,
                "name" to name
            )).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Message not found"))
                .build()
        }
    }

    @GET
    @Path("/user/{action}")
    fun getUserMessage(
        @PathParam("action") action: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getMessageUseCase.execute(GetMessageRequest.User(action, locale))
        
        return if (response.found) {
            Response.ok(mapOf(
                "message" to response.message!!.resolve(),
                "locale" to response.message.locale.language,
                "action" to action
            )).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Message not found"))
                .build()
        }
    }

    @GET
    @Path("/error/{category}/{type}")
    fun getErrorMessage(
        @PathParam("category") category: String,
        @PathParam("type") type: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getMessageUseCase.execute(GetMessageRequest.Error(category, type, locale))
        
        return if (response.found) {
            Response.ok(mapOf(
                "message" to response.message!!.resolve(),
                "locale" to response.message.locale.language,
                "category" to category,
                "type" to type
            )).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Message not found"))
                .build()
        }
    }

    @GET
    @Path("/success/{type}")
    fun getSuccessMessage(
        @PathParam("type") type: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getMessageUseCase.execute(GetMessageRequest.Success(type, locale))
        
        return if (response.found) {
            Response.ok(mapOf(
                "message" to response.message!!.resolve(),
                "locale" to response.message.locale.language,
                "type" to type
            )).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Message not found"))
                .build()
        }
    }

    @GET
    @Path("/messages")
    fun getAllMessages(
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = determineLocale(lang, headers)
        val response = getAllMessagesUseCase.execute(org.it.business.application.usecase.GetAllMessagesRequest(locale))
        
        return Response.ok(mapOf(
            "locale" to response.locale.language,
            "messages" to response.messages.mapKeys { it.key.toString() },
            "count" to response.count
        )).build()
    }
    
    private fun determineLocale(lang: String?, headers: HttpHeaders): Locale {
        return if (lang != null) {
            Locale.of(lang)
        } else {
            val acceptLanguage = headers.getRequestHeader("Accept-Language")?.firstOrNull()
            detectLocaleUseCase.execute(org.it.business.application.usecase.DetectLocaleRequest(acceptLanguage)).detectedLocale
        }
    }
}
