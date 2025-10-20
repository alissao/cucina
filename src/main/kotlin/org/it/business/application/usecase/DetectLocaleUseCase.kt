package org.it.business.application.usecase

import org.it.business.application.port.MessageServicePort
import org.it.business.domain.valueobject.Locale

/**
 * Use case for detecting locale from HTTP headers
 */
class DetectLocaleUseCase(
    private val messageService: MessageServicePort
) {
    
    fun execute(request: DetectLocaleRequest): DetectLocaleResponse {
        val locale = messageService.detectLocaleFromHeaders(request.acceptLanguage)
        
        return DetectLocaleResponse(
            detectedLocale = locale,
            source = request.source
        )
    }
}

/**
 * Request for locale detection
 */
data class DetectLocaleRequest(
    val acceptLanguage: String?,
    val source: String = "http_header"
)

/**
 * Response for locale detection
 */
data class DetectLocaleResponse(
    val detectedLocale: Locale,
    val source: String
)
