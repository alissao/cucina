package org.it.business.domain.valueobject

/**
 * Value object representing a message key
 */
data class MessageKey private constructor(
    val key: String
) {
    
    companion object {
        fun of(key: String): MessageKey {
            require(key.isNotBlank()) { "Message key cannot be blank" }
            return MessageKey(key.trim())
        }
        
        // Predefined message keys
        val WELCOME = MessageKey("welcome")
        val GREETING = MessageKey("greeting")
        val USER_CREATED = MessageKey("user.created")
        val USER_UPDATED = MessageKey("user.updated")
        val USER_DELETED = MessageKey("user.deleted")
        val USER_NOT_FOUND = MessageKey("user.not_found")
        val USER_ALREADY_EXISTS = MessageKey("user.already_exists")
        val ERROR_VALIDATION_REQUIRED = MessageKey("error.validation.required")
        val ERROR_VALIDATION_INVALID_EMAIL = MessageKey("error.validation.invalid_email")
        val ERROR_VALIDATION_PASSWORD_TOO_SHORT = MessageKey("error.validation.password_too_short")
        val ERROR_VALIDATION_PASSWORD_MISMATCH = MessageKey("error.validation.password_mismatch")
        val ERROR_SERVER_INTERNAL = MessageKey("error.server.internal")
        val ERROR_SERVER_UNAUTHORIZED = MessageKey("error.server.unauthorized")
        val ERROR_SERVER_FORBIDDEN = MessageKey("error.server.forbidden")
        val ERROR_SERVER_NOT_FOUND = MessageKey("error.server.not_found")
        val SUCCESS_OPERATION_COMPLETED = MessageKey("success.operation_completed")
        val SUCCESS_DATA_SAVED = MessageKey("success.data_saved")
        val SUCCESS_DATA_DELETED = MessageKey("success.data_deleted")
    }
    
    override fun toString(): String = key
}
