package org.it.business.application.usecase

import org.it.business.application.port.MessageServicePort
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.Message
import org.it.business.domain.valueobject.MessageKey

/**
 * Use case for getting messages
 */
class GetMessageUseCase(
    private val messageService: MessageServicePort
) {
    
    fun execute(request: GetMessageRequest): GetMessageResponse {
        val message = when (request) {
            is GetMessageRequest.ByKey -> {
                messageService.getMessage(request.key, request.locale)
            }
            is GetMessageRequest.WithParams -> {
                messageService.getMessageWithParams(request.key, request.parameters, request.locale)
            }
            is GetMessageRequest.Greeting -> {
                messageService.getGreeting(request.name, request.locale)
            }
            is GetMessageRequest.Welcome -> {
                messageService.getWelcomeMessage(request.locale)
            }
            is GetMessageRequest.User -> {
                messageService.getUserMessage(request.action, request.locale)
            }
            is GetMessageRequest.Error -> {
                messageService.getErrorMessage(request.category, request.type, request.locale)
            }
            is GetMessageRequest.Success -> {
                messageService.getSuccessMessage(request.type, request.locale)
            }
        }
        
        return GetMessageResponse(
            message = message,
            found = message != null
        )
    }
}

