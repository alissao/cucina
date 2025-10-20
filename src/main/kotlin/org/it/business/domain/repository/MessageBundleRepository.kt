package org.it.business.domain.repository

import org.it.business.domain.entity.MessageBundle
import org.it.business.domain.entity.MessageBundleId
import org.it.business.domain.valueobject.Locale

/**
 * Repository interface for MessageBundle persistence
 * This is a port in the hexagonal architecture
 */
interface MessageBundleRepository {
    
    /**
     * Finds a message bundle by its ID
     */
    fun findById(id: MessageBundleId): MessageBundle?
    
    /**
     * Finds a message bundle by locale
     */
    fun findByLocale(locale: Locale): MessageBundle?
    
    /**
     * Finds all message bundles
     */
    fun findAll(): List<MessageBundle>
    
    /**
     * Saves a message bundle
     */
    fun save(messageBundle: MessageBundle): MessageBundle
    
    /**
     * Deletes a message bundle by ID
     */
    fun deleteById(id: MessageBundleId): Boolean
    
    /**
     * Checks if a message bundle exists for the given locale
     */
    fun existsByLocale(locale: Locale): Boolean
    
    /**
     * Finds message bundles by locale language
     */
    fun findByLanguage(language: String): List<MessageBundle>
}
