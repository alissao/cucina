package org.it.business.application.port

import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.Message
import org.it.business.domain.valueobject.MessageKey

/**
 * Port interface for message service operations
 * This defines the contract for the application layer
 */
interface MessageServicePort {
    
    /**
     * Gets a message by key and locale
     */
    fun getMessage(key: MessageKey, locale: Locale): Message?
    
    /**
     * Gets a message with parameters by key and locale
     */
    fun getMessageWithParams(
        key: MessageKey, 
        parameters: Map<String, String>, 
        locale: Locale
    ): Message?
    
    /**
     * Gets a greeting message with name parameter
     */
    fun getGreeting(name: String, locale: Locale): Message?
    
    /**
     * Gets a welcome message
     */
    fun getWelcomeMessage(locale: Locale): Message?
    
    /**
     * Gets a user message by action
     */
    fun getUserMessage(action: String, locale: Locale): Message?
    
    /**
     * Gets an error message by category and type
     */
    fun getErrorMessage(category: String, type: String, locale: Locale): Message?
    
    /**
     * Gets a success message by type
     */
    fun getSuccessMessage(type: String, locale: Locale): Message?
    
    /**
     * Gets all messages for a locale
     */
    fun getAllMessages(locale: Locale): Map<MessageKey, String>
    
    /**
     * Detects locale from HTTP headers
     */
    fun detectLocaleFromHeaders(acceptLanguage: String?): Locale
}
