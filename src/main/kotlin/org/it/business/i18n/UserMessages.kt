package org.it.business.i18n

data class UserMessages(
    val welcome: String,
    val greeting: String,
    val user: UserSection,
    val error: ErrorSection,
    val success: SuccessSection
)

data class UserSection(
    val created: String,
    val updated: String,
    val deleted: String,
    val notFound: String,
    val alreadyExists: String
)

data class ErrorSection(
    val validation: ValidationSection,
    val server: ServerSection
)

data class ValidationSection(
    val required: String,
    val invalidEmail: String,
    val passwordTooShort: String,
    val passwordMismatch: String
)

data class ServerSection(
    val internal: String,
    val unauthorized: String,
    val forbidden: String,
    val notFound: String
)

data class SuccessSection(
    val operationCompleted: String,
    val dataSaved: String,
    val dataDeleted: String
)
