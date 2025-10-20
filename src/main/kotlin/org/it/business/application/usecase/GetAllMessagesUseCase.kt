package org.it.business.application.usecase

import org.it.business.application.port.MessageServicePort
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey

/**
 * Use case for getting all messages for a locale
 */
class GetAllMessagesUseCase(
    private val messageService: MessageServicePort
) {
    
    fun execute(request: GetAllMessagesRequest): GetAllMessagesResponse {
        val messages = messageService.getAllMessages(request.locale)
        
        return GetAllMessagesResponse(
            locale = request.locale,
            messages = messages,
            count = messages.size
        )
    }
}

/**
 * Request for getting all messages
 */
data class GetAllMessagesRequest(
    val locale: Locale
)

/**
 * Response containing all messages for a locale
 */
data class GetAllMessagesResponse(
    val locale: Locale,
    val messages: Map<MessageKey, String>,
    val count: Int
)
