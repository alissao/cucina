package org.it.business.application.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.it.business.application.port.MessageServicePort
import org.it.business.domain.repository.MessageBundleRepository
import org.it.business.domain.service.MessageResolutionService
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.Message
import org.it.business.domain.valueobject.MessageKey
import java.util.*

/**
 * Application service implementing MessageServicePort
 * This is the core application service in the hexagonal architecture
 */
@ApplicationScoped
class MessageService @Inject constructor(
    private val messageBundleRepository: MessageBundleRepository,
    private val messageResolutionService: MessageResolutionService
) : MessageServicePort {
    
    override fun getMessage(key: MessageKey, locale: Locale): Message? {
        val bundles = messageBundleRepository.findAll().associateBy { it.locale }
        return messageResolutionService.resolveMessage(key, locale, bundles)
    }
    
    override fun getMessageWithParams(
        key: MessageKey, 
        parameters: Map<String, String>, 
        locale: Locale
    ): Message? {
        val message = getMessage(key, locale)
        return message?.withParameters(parameters)
    }
    
    override fun getGreeting(name: String, locale: Locale): Message? {
        return getMessageWithParams(
            MessageKey.GREETING, 
            mapOf("name" to name), 
            locale
        )
    }
    
    override fun getWelcomeMessage(locale: Locale): Message? {
        return getMessage(MessageKey.WELCOME, locale)
    }
    
    override fun getUserMessage(action: String, locale: Locale): Message? {
        val key = when (action) {
            "created" -> MessageKey.USER_CREATED
            "updated" -> MessageKey.USER_UPDATED
            "deleted" -> MessageKey.USER_DELETED
            "not_found" -> MessageKey.USER_NOT_FOUND
            "already_exists" -> MessageKey.USER_ALREADY_EXISTS
            else -> MessageKey.of("user.$action")
        }
        return getMessage(key, locale)
    }
    
    override fun getErrorMessage(category: String, type: String, locale: Locale): Message? {
        val key = when {
            category == "validation" && type == "required" -> MessageKey.ERROR_VALIDATION_REQUIRED
            category == "validation" && type == "invalid_email" -> MessageKey.ERROR_VALIDATION_INVALID_EMAIL
            category == "validation" && type == "password_too_short" -> MessageKey.ERROR_VALIDATION_PASSWORD_TOO_SHORT
            category == "validation" && type == "password_mismatch" -> MessageKey.ERROR_VALIDATION_PASSWORD_MISMATCH
            category == "server" && type == "internal" -> MessageKey.ERROR_SERVER_INTERNAL
            category == "server" && type == "unauthorized" -> MessageKey.ERROR_SERVER_UNAUTHORIZED
            category == "server" && type == "forbidden" -> MessageKey.ERROR_SERVER_FORBIDDEN
            category == "server" && type == "not_found" -> MessageKey.ERROR_SERVER_NOT_FOUND
            else -> MessageKey.of("error.$category.$type")
        }
        return getMessage(key, locale)
    }
    
    override fun getSuccessMessage(type: String, locale: Locale): Message? {
        val key = when (type) {
            "operation_completed" -> MessageKey.SUCCESS_OPERATION_COMPLETED
            "data_saved" -> MessageKey.SUCCESS_DATA_SAVED
            "data_deleted" -> MessageKey.SUCCESS_DATA_DELETED
            else -> MessageKey.of("success.$type")
        }
        return getMessage(key, locale)
    }
    
    override fun getAllMessages(locale: Locale): Map<MessageKey, String> {
        val bundle = messageBundleRepository.findByLocale(locale)
            ?: messageBundleRepository.findByLocale(Locale.ENGLISH)
            ?: return emptyMap()
        
        return bundle.messages
    }
    
    override fun detectLocaleFromHeaders(acceptLanguage: String?): Locale {
        if (acceptLanguage.isNullOrBlank()) {
            return Locale.ENGLISH
        }
        
        val localeString = acceptLanguage.split(",")[0].split(";")[0].trim()
        return when (localeString.lowercase()) {
            "es", "es-es" -> Locale.SPANISH
            "fr", "fr-fr" -> Locale.FRENCH
            else -> Locale.ENGLISH
        }
    }
}
