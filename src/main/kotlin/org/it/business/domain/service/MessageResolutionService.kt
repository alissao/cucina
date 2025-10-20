package org.it.business.domain.service

import org.it.business.domain.entity.MessageBundle
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey
import org.it.business.domain.valueobject.Message

/**
 * Domain service for message resolution logic
 */
class MessageResolutionService {
    
    /**
     * Resolves a message with fallback logic
     */
    fun resolveMessage(
        key: MessageKey,
        locale: Locale,
        bundles: Map<Locale, MessageBundle>
    ): Message? {
        // Try exact locale match first
        bundles[locale]?.getMessage(key)?.let { text ->
            return Message(key, text, locale)
        }
        
        // Try fallback to language only (e.g., en-US -> en)
        if (locale.country.isNotEmpty()) {
            val languageOnly = Locale.of(locale.language)
            bundles[languageOnly]?.getMessage(key)?.let { text ->
                return Message(key, text, languageOnly)
            }
        }
        
        // Try fallback to default locale (English)
        bundles[Locale.ENGLISH]?.getMessage(key)?.let { text ->
            return Message(key, text, Locale.ENGLISH)
        }
        
        return null
    }
    
    /**
     * Validates message bundles for completeness
     */
    fun validateBundles(bundles: Map<Locale, MessageBundle>): ValidationResult {
        val requiredKeys = getAllRequiredMessageKeys()
        val errors = mutableListOf<String>()
        
        bundles.forEach { (locale, bundle) ->
            val missingKeys = requiredKeys - bundle.messages.keys
            if (missingKeys.isNotEmpty()) {
                errors.add("Bundle for locale ${locale} is missing keys: ${missingKeys.joinToString(", ")}")
            }
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.valid()
        } else {
            ValidationResult.invalid(errors)
        }
    }
    
    /**
     * Finds missing translations between bundles
     */
    fun findMissingTranslations(bundles: Map<Locale, MessageBundle>): Map<Locale, Set<MessageKey>> {
        val allKeys = bundles.values.flatMap { it.messages.keys }.toSet()
        val missingTranslations = mutableMapOf<Locale, Set<MessageKey>>()
        
        bundles.forEach { (locale, bundle) ->
            val missing = allKeys - bundle.messages.keys
            if (missing.isNotEmpty()) {
                missingTranslations[locale] = missing
            }
        }
        
        return missingTranslations
    }
}

/**
 * Result of bundle validation
 */
data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    companion object {
        fun valid(): ValidationResult = ValidationResult(true)
        fun invalid(errors: List<String>): ValidationResult = ValidationResult(false, errors)
    }
}

/**
 * Extension to get all predefined MessageKey values
 */
private fun getAllRequiredMessageKeys(): Set<org.it.business.domain.valueobject.MessageKey> {
    return setOf(
        org.it.business.domain.valueobject.MessageKey.WELCOME,
        org.it.business.domain.valueobject.MessageKey.GREETING,
        org.it.business.domain.valueobject.MessageKey.USER_CREATED,
        org.it.business.domain.valueobject.MessageKey.USER_UPDATED,
        org.it.business.domain.valueobject.MessageKey.USER_DELETED,
        org.it.business.domain.valueobject.MessageKey.USER_NOT_FOUND,
        org.it.business.domain.valueobject.MessageKey.USER_ALREADY_EXISTS,
        org.it.business.domain.valueobject.MessageKey.ERROR_VALIDATION_REQUIRED,
        org.it.business.domain.valueobject.MessageKey.ERROR_VALIDATION_INVALID_EMAIL,
        org.it.business.domain.valueobject.MessageKey.ERROR_VALIDATION_PASSWORD_TOO_SHORT,
        org.it.business.domain.valueobject.MessageKey.ERROR_VALIDATION_PASSWORD_MISMATCH,
        org.it.business.domain.valueobject.MessageKey.ERROR_SERVER_INTERNAL,
        org.it.business.domain.valueobject.MessageKey.ERROR_SERVER_UNAUTHORIZED,
        org.it.business.domain.valueobject.MessageKey.ERROR_SERVER_FORBIDDEN,
        org.it.business.domain.valueobject.MessageKey.ERROR_SERVER_NOT_FOUND,
        org.it.business.domain.valueobject.MessageKey.SUCCESS_OPERATION_COMPLETED,
        org.it.business.domain.valueobject.MessageKey.SUCCESS_DATA_SAVED,
        org.it.business.domain.valueobject.MessageKey.SUCCESS_DATA_DELETED
    )
}
