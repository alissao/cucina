package org.it.business.infrastructure.persistence

import jakarta.enterprise.context.ApplicationScoped
import org.it.business.domain.entity.MessageBundle
import org.it.business.domain.entity.MessageBundleId
import org.it.business.domain.repository.MessageBundleRepository
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.time.Instant

/**
 * Infrastructure adapter implementing MessageBundleRepository using YAML files
 * This is an adapter in the hexagonal architecture
 */
@ApplicationScoped
class YamlMessageBundleRepository : MessageBundleRepository {
    
    private val yaml = Yaml()
    private val messageCache = mutableMapOf<String, MessageBundle>()
    
    override fun findById(id: MessageBundleId): MessageBundle? {
        return messageCache[id.value]
    }
    
    override fun findByLocale(locale: Locale): MessageBundle? {
        val localeKey = locale.language.lowercase()
        return messageCache.getOrPut(localeKey) {
            loadMessagesFromYaml(localeKey)
        }
    }
    
    override fun findAll(): List<MessageBundle> {
        // Load all available locales
        val availableLocales = listOf("en", "es", "fr")
        return availableLocales.mapNotNull { locale ->
            findByLocale(Locale.of(locale))
        }
    }
    
    override fun save(messageBundle: MessageBundle): MessageBundle {
        messageCache[messageBundle.id.value] = messageBundle
        return messageBundle
    }
    
    override fun deleteById(id: MessageBundleId): Boolean {
        return messageCache.remove(id.value) != null
    }
    
    override fun existsByLocale(locale: Locale): Boolean {
        return findByLocale(locale) != null
    }
    
    override fun findByLanguage(language: String): List<MessageBundle> {
        return messageCache.values.filter { it.locale.language == language.lowercase() }
    }
    
    private fun loadMessagesFromYaml(locale: String): MessageBundle {
        val resourceName = "data/messages_${locale}.yaml"
        val inputStream: InputStream = Thread.currentThread().contextClassLoader
            .getResourceAsStream(resourceName)
            ?: Thread.currentThread().contextClassLoader
                .getResourceAsStream("data/messages_en.yaml")
            ?: throw RuntimeException("Could not find message file for locale: $locale")
        
        @Suppress("UNCHECKED_CAST")
        val data = yaml.load(inputStream) as Map<String, Any>
        
        val messages = mutableMapOf<MessageKey, String>()
        
        // Map YAML data to MessageKey enum
        data["welcome"]?.let { messages[MessageKey.WELCOME] = it as String }
        data["greeting"]?.let { messages[MessageKey.GREETING] = it as String }
        
        // User messages
        (data["user"] as? Map<String, Any>)?.let { userData ->
            userData["created"]?.let { messages[MessageKey.USER_CREATED] = it as String }
            userData["updated"]?.let { messages[MessageKey.USER_UPDATED] = it as String }
            userData["deleted"]?.let { messages[MessageKey.USER_DELETED] = it as String }
            userData["not_found"]?.let { messages[MessageKey.USER_NOT_FOUND] = it as String }
            userData["already_exists"]?.let { messages[MessageKey.USER_ALREADY_EXISTS] = it as String }
        }
        
        // Error messages
        (data["error"] as? Map<String, Any>)?.let { errorData ->
            // Validation errors
            ((errorData["validation"] as? Map<String, Any>))?.let { validationData ->
                validationData["required"]?.let { messages[MessageKey.ERROR_VALIDATION_REQUIRED] = it as String }
                validationData["invalid_email"]?.let { messages[MessageKey.ERROR_VALIDATION_INVALID_EMAIL] = it as String }
                validationData["password_too_short"]?.let { messages[MessageKey.ERROR_VALIDATION_PASSWORD_TOO_SHORT] = it as String }
                validationData["password_mismatch"]?.let { messages[MessageKey.ERROR_VALIDATION_PASSWORD_MISMATCH] = it as String }
            }
            
            // Server errors
            ((errorData["server"] as? Map<String, Any>))?.let { serverData ->
                serverData["internal"]?.let { messages[MessageKey.ERROR_SERVER_INTERNAL] = it as String }
                serverData["unauthorized"]?.let { messages[MessageKey.ERROR_SERVER_UNAUTHORIZED] = it as String }
                serverData["forbidden"]?.let { messages[MessageKey.ERROR_SERVER_FORBIDDEN] = it as String }
                serverData["not_found"]?.let { messages[MessageKey.ERROR_SERVER_NOT_FOUND] = it as String }
            }
        }
        
        // Success messages
        (data["success"] as? Map<String, Any>)?.let { successData ->
            successData["operation_completed"]?.let { messages[MessageKey.SUCCESS_OPERATION_COMPLETED] = it as String }
            successData["data_saved"]?.let { messages[MessageKey.SUCCESS_DATA_SAVED] = it as String }
            successData["data_deleted"]?.let { messages[MessageKey.SUCCESS_DATA_DELETED] = it as String }
        }
        
        val bundleLocale = Locale.of(locale)
        return MessageBundle(
            id = MessageBundleId.fromLocale(bundleLocale),
            locale = bundleLocale,
            messages = messages,
            lastUpdated = Instant.now()
        )
    }
}
