package org.it.business.infrastructure.config

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.it.business.application.port.MessageServicePort
import org.it.business.application.service.MessageService
import org.it.business.application.usecase.DetectLocaleUseCase
import org.it.business.application.usecase.GetAllMessagesUseCase
import org.it.business.application.usecase.GetMessageUseCase
import org.it.business.domain.repository.MessageBundleRepository
import org.it.business.domain.service.MessageResolutionService
import org.it.business.infrastructure.persistence.YamlMessageBundleRepository

/**
 * Configuration class for hexagonal architecture dependency injection
 * This class wires up all the dependencies following the hexagonal architecture pattern
 */
@ApplicationScoped
class HexagonalConfiguration {
    
    /**
     * Produces the MessageResolutionService
     */
    @Produces
    @ApplicationScoped
    fun messageResolutionService(): MessageResolutionService {
        return MessageResolutionService()
    }
    
    /**
     * Produces the GetMessageUseCase
     */
    @Produces
    @ApplicationScoped
    fun getMessageUseCase(messageService: MessageServicePort): GetMessageUseCase {
        return GetMessageUseCase(messageService)
    }
    
    /**
     * Produces the GetAllMessagesUseCase
     */
    @Produces
    @ApplicationScoped
    fun getAllMessagesUseCase(messageService: MessageServicePort): GetAllMessagesUseCase {
        return GetAllMessagesUseCase(messageService)
    }
    
    /**
     * Produces the DetectLocaleUseCase
     */
    @Produces
    @ApplicationScoped
    fun detectLocaleUseCase(messageService: MessageServicePort): DetectLocaleUseCase {
        return DetectLocaleUseCase(messageService)
    }
}
