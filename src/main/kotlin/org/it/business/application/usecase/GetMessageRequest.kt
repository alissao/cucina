package org.it.business.application.usecase

import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey

/**
 * Request types for getting messages
 */
sealed class GetMessageRequest {
    data class ByKey(
        val key: MessageKey,
        val locale: Locale
    ) : GetMessageRequest()
    
    data class WithParams(
        val key: MessageKey,
        val parameters: Map<String, String>,
        val locale: Locale
    ) : GetMessageRequest()
    
    data class Greeting(
        val name: String,
        val locale: Locale
    ) : GetMessageRequest()
    
    data class Welcome(
        val locale: Locale
    ) : GetMessageRequest()
    
    data class User(
        val action: String,
        val locale: Locale
    ) : GetMessageRequest()
    
    data class Error(
        val category: String,
        val type: String,
        val locale: Locale
    ) : GetMessageRequest()
    
    data class Success(
        val type: String,
        val locale: Locale
    ) : GetMessageRequest()
}

/**
 * Response for message requests
 */
data class GetMessageResponse(
    val message: org.it.business.domain.valueobject.Message?,
    val found: Boolean
)
