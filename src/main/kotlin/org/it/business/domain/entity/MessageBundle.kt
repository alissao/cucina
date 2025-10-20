package org.it.business.domain.entity

import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey
import java.time.Instant

/**
 * Domain entity representing a bundle of messages for a specific locale
 */
data class MessageBundle(
    val id: MessageBundleId,
    val locale: Locale,
    val messages: Map<MessageKey, String>,
    val lastUpdated: Instant = Instant.now()
) {
    
    fun getMessage(key: MessageKey): String? {
        return messages[key]
    }
    
    fun hasMessage(key: MessageKey): Boolean {
        return messages.containsKey(key)
    }
    
    fun addMessage(key: MessageKey, text: String): MessageBundle {
        return copy(
            messages = messages + (key to text),
            lastUpdated = Instant.now()
        )
    }
    
    fun removeMessage(key: MessageKey): MessageBundle {
        return copy(
            messages = messages - key,
            lastUpdated = Instant.now()
        )
    }
    
    fun updateMessage(key: MessageKey, text: String): MessageBundle {
        require(messages.containsKey(key)) { "Message key $key does not exist in bundle" }
        return addMessage(key, text)
    }
}

/**
 * Value object for MessageBundle identifier
 */
@JvmInline
value class MessageBundleId(val value: String) {
    init {
        require(value.isNotBlank()) { "MessageBundleId cannot be blank" }
    }
    
    companion object {
        fun fromLocale(locale: Locale): MessageBundleId {
            return MessageBundleId(locale.language)
        }
    }
}
