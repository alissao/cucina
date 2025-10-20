package org.it.business

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.it.business.application.port.MessageServicePort
import org.it.business.domain.repository.MessageBundleRepository
import org.it.business.domain.valueobject.Locale
import org.it.business.domain.valueobject.MessageKey
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Test to verify the hexagonal architecture is working correctly
 */
@QuarkusTest
class HexagonalArchitectureTest {

    @Inject
    lateinit var messageService: MessageServicePort

    @Inject
    lateinit var messageBundleRepository: MessageBundleRepository

    @Test
    fun `should inject message service port`() {
        assertNotNull(messageService, "MessageServicePort should be injected")
    }

    @Test
    fun `should inject message bundle repository`() {
        assertNotNull(messageBundleRepository, "MessageBundleRepository should be injected")
    }

    @Test
    fun `should get welcome message`() {
        val message = messageService.getWelcomeMessage(Locale.ENGLISH)
        assertNotNull(message, "Welcome message should not be null")
        assertEquals(Locale.ENGLISH, message?.locale, "Message locale should be English")
        assertTrue(message?.resolve()?.isNotEmpty() == true, "Message text should not be empty")
    }

    @Test
    fun `should get greeting message with parameters`() {
        val message = messageService.getGreeting("John", Locale.ENGLISH)
        assertNotNull(message, "Greeting message should not be null")
        assertEquals(Locale.ENGLISH, message?.locale, "Message locale should be English")
        assertTrue(message?.resolve()?.contains("John") == true, "Message should contain the name parameter")
    }

    @Test
    fun `should find message bundles by locale`() {
        val bundle = messageBundleRepository.findByLocale(Locale.ENGLISH)
        assertNotNull(bundle, "English message bundle should exist")
        assertEquals(Locale.ENGLISH, bundle?.locale, "Bundle locale should be English")
        assertTrue(bundle?.messages?.isNotEmpty() == true, "Bundle should contain messages")
    }

    @Test
    fun `should get all message bundles`() {
        val bundles = messageBundleRepository.findAll()
        assertTrue(bundles.isNotEmpty(), "Should have at least one message bundle")
        assertTrue(bundles.any { it.locale == Locale.ENGLISH }, "Should have English bundle")
    }
}
