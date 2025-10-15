package org.it.business.i18n

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import java.util.*

@ApplicationScoped
class MessageService {
    
    private val yaml = Yaml()
    private val messageCache = mutableMapOf<String, UserMessages>()

    fun getMessage(key: String, locale: Locale = Locale.getDefault()): String {
        val messages = getMessagesForLocale(locale)
        return getNestedValue(messages, key) ?: key
    }

    fun getMessageWithParams(key: String, params: Map<String, String>, locale: Locale = Locale.getDefault()): String {
        var message = getMessage(key, locale)
        params.forEach { (paramKey, value) ->
            message = message.replace("{$paramKey}", value)
        }
        return message
    }

    fun getGreeting(name: String, locale: Locale = Locale.getDefault()): String {
        return getMessageWithParams("greeting", mapOf("name" to name), locale)
    }

    fun getWelcomeMessage(locale: Locale = Locale.getDefault()): String {
        return getMessage("welcome", locale)
    }

    fun getUserMessage(key: String, locale: Locale = Locale.getDefault()): String {
        return getMessage("user.$key", locale)
    }

    fun getErrorMessage(key: String, locale: Locale = Locale.getDefault()): String {
        return getMessage("error.$key", locale)
    }

    fun getSuccessMessage(key: String, locale: Locale = Locale.getDefault()): String {
        return getMessage("success.$key", locale)
    }

    private fun getMessagesForLocale(locale: Locale): UserMessages {
        val localeKey = locale.language.lowercase()
        return messageCache.getOrPut(localeKey) {
            loadMessagesFromYaml(localeKey)
        }
    }
    
    private fun loadMessagesFromYaml(locale: String): UserMessages {
        val resourceName = "data/messages_${locale}.yaml"
        val inputStream: InputStream = Thread.currentThread().contextClassLoader
            .getResourceAsStream(resourceName)
            ?: Thread.currentThread().contextClassLoader
                .getResourceAsStream("data/messages_en.yaml")
            ?: throw RuntimeException("Could not find message file for locale: $locale")
        
        @Suppress("UNCHECKED_CAST")
        val data = yaml.load(inputStream) as Map<String, Any>
        
        return UserMessages(
            welcome = data["welcome"] as String,
            greeting = data["greeting"] as String,
            user = UserSection(
                created = (data["user"] as Map<String, Any>)["created"] as String,
                updated = (data["user"] as Map<String, Any>)["updated"] as String,
                deleted = (data["user"] as Map<String, Any>)["deleted"] as String,
                notFound = (data["user"] as Map<String, Any>)["not_found"] as String,
                alreadyExists = (data["user"] as Map<String, Any>)["already_exists"] as String
            ),
            error = ErrorSection(
                validation = ValidationSection(
                    required = ((data["error"] as Map<String, Any>)["validation"] as Map<String, Any>)["required"] as String,
                    invalidEmail = ((data["error"] as Map<String, Any>)["validation"] as Map<String, Any>)["invalid_email"] as String,
                    passwordTooShort = ((data["error"] as Map<String, Any>)["validation"] as Map<String, Any>)["password_too_short"] as String,
                    passwordMismatch = ((data["error"] as Map<String, Any>)["validation"] as Map<String, Any>)["password_mismatch"] as String
                ),
                server = ServerSection(
                    internal = ((data["error"] as Map<String, Any>)["server"] as Map<String, Any>)["internal"] as String,
                    unauthorized = ((data["error"] as Map<String, Any>)["server"] as Map<String, Any>)["unauthorized"] as String,
                    forbidden = ((data["error"] as Map<String, Any>)["server"] as Map<String, Any>)["forbidden"] as String,
                    notFound = ((data["error"] as Map<String, Any>)["server"] as Map<String, Any>)["not_found"] as String
                )
            ),
            success = SuccessSection(
                operationCompleted = (data["success"] as Map<String, Any>)["operation_completed"] as String,
                dataSaved = (data["success"] as Map<String, Any>)["data_saved"] as String,
                dataDeleted = (data["success"] as Map<String, Any>)["data_deleted"] as String
            )
        )
    }

    private fun getNestedValue(messages: UserMessages, key: String): String? {
        val keys = key.split(".")
        return when (keys[0]) {
            "welcome" -> messages.welcome
            "greeting" -> messages.greeting
            "user" -> when (keys[1]) {
                "created" -> messages.user.created
                "updated" -> messages.user.updated
                "deleted" -> messages.user.deleted
                "notFound" -> messages.user.notFound
                "alreadyExists" -> messages.user.alreadyExists
                else -> null
            }
            "error" -> when (keys[1]) {
                "validation" -> when (keys[2]) {
                    "required" -> messages.error.validation.required
                    "invalidEmail" -> messages.error.validation.invalidEmail
                    "passwordTooShort" -> messages.error.validation.passwordTooShort
                    "passwordMismatch" -> messages.error.validation.passwordMismatch
                    else -> null
                }
                "server" -> when (keys[2]) {
                    "internal" -> messages.error.server.internal
                    "unauthorized" -> messages.error.server.unauthorized
                    "forbidden" -> messages.error.server.forbidden
                    "notFound" -> messages.error.server.notFound
                    else -> null
                }
                else -> null
            }
            "success" -> when (keys[1]) {
                "operationCompleted" -> messages.success.operationCompleted
                "dataSaved" -> messages.success.dataSaved
                "dataDeleted" -> messages.success.dataDeleted
                else -> null
            }
            else -> null
        }
    }

    fun detectLocaleFromHeaders(headers: HttpHeaders): Locale {
        val acceptLanguage = headers.getRequestHeader("Accept-Language")
        if (acceptLanguage != null && acceptLanguage.isNotEmpty()) {
            val localeString = acceptLanguage[0].split(",")[0].split(";")[0].trim()
            return when (localeString.lowercase()) {
                "es", "es-es" -> Locale("es")
                "fr", "fr-fr" -> Locale("fr")
                else -> Locale("en")
            }
        }
        return Locale.getDefault()
    }
}
