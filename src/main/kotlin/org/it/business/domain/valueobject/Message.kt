package org.it.business.domain.valueobject

/**
 * Value object representing a localized message
 */
data class Message(
    val key: MessageKey,
    val text: String,
    val locale: Locale,
    val parameters: Map<String, String> = emptyMap()
) {
    
    fun withParameters(parameters: Map<String, String>): Message {
        return copy(parameters = parameters)
    }
    
    fun withParameter(key: String, value: String): Message {
        return copy(parameters = this.parameters + (key to value))
    }
    
    /**
     * Resolves parameters in the message text
     */
    fun resolve(): String {
        var resolvedText = text
        parameters.forEach { (paramKey, value) ->
            resolvedText = resolvedText.replace("{$paramKey}", value)
        }
        return resolvedText
    }
}
