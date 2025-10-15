package org.it.business.i18n

import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.util.*

@Path("/api/i18n")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class I18nResource @Inject constructor(
    private val messageService: MessageService
) {

    @GET
    @Path("/welcome")
    fun getWelcomeMessage(
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val message = messageService.getWelcomeMessage(locale)
        
        return Response.ok(mapOf(
            "message" to message,
            "locale" to locale.language
        )).build()
    }

    @GET
    @Path("/greeting")
    fun getGreeting(
        @QueryParam("name") name: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val message = messageService.getGreeting(name, locale)
        
        return Response.ok(mapOf(
            "message" to message,
            "locale" to locale.language,
            "name" to name
        )).build()
    }

    @GET
    @Path("/user/{action}")
    fun getUserMessage(
        @PathParam("action") action: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val message = messageService.getUserMessage(action, locale)
        
        return Response.ok(mapOf(
            "message" to message,
            "locale" to locale.language,
            "action" to action
        )).build()
    }

    @GET
    @Path("/error/{category}/{type}")
    fun getErrorMessage(
        @PathParam("category") category: String,
        @PathParam("type") type: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val message = messageService.getErrorMessage("$category.$type", locale)
        
        return Response.ok(mapOf(
            "message" to message,
            "locale" to locale.language,
            "category" to category,
            "type" to type
        )).build()
    }

    @GET
    @Path("/success/{type}")
    fun getSuccessMessage(
        @PathParam("type") type: String,
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val message = messageService.getSuccessMessage(type, locale)
        
        return Response.ok(mapOf(
            "message" to message,
            "locale" to locale.language,
            "type" to type
        )).build()
    }

    @GET
    @Path("/messages")
    fun getAllMessages(
        @QueryParam("lang") lang: String?,
        @Context headers: HttpHeaders
    ): Response {
        val locale = if (lang != null) {
            Locale(lang)
        } else {
            messageService.detectLocaleFromHeaders(headers)
        }
        
        val messages = mapOf(
            "welcome" to messageService.getWelcomeMessage(locale),
            "greeting" to messageService.getGreeting("User", locale),
            "user" to mapOf(
                "created" to messageService.getUserMessage("created", locale),
                "updated" to messageService.getUserMessage("updated", locale),
                "deleted" to messageService.getUserMessage("deleted", locale),
                "notFound" to messageService.getUserMessage("notFound", locale),
                "alreadyExists" to messageService.getUserMessage("alreadyExists", locale)
            ),
            "error" to mapOf(
                "validation" to mapOf(
                    "required" to messageService.getErrorMessage("validation.required", locale),
                    "invalidEmail" to messageService.getErrorMessage("validation.invalidEmail", locale),
                    "passwordTooShort" to messageService.getErrorMessage("validation.passwordTooShort", locale),
                    "passwordMismatch" to messageService.getErrorMessage("validation.passwordMismatch", locale)
                ),
                "server" to mapOf(
                    "internal" to messageService.getErrorMessage("server.internal", locale),
                    "unauthorized" to messageService.getErrorMessage("server.unauthorized", locale),
                    "forbidden" to messageService.getErrorMessage("server.forbidden", locale),
                    "notFound" to messageService.getErrorMessage("server.notFound", locale)
                )
            ),
            "success" to mapOf(
                "operationCompleted" to messageService.getSuccessMessage("operationCompleted", locale),
                "dataSaved" to messageService.getSuccessMessage("dataSaved", locale),
                "dataDeleted" to messageService.getSuccessMessage("dataDeleted", locale)
            )
        )
        
        return Response.ok(mapOf(
            "messages" to messages,
            "locale" to locale.language
        )).build()
    }
}
