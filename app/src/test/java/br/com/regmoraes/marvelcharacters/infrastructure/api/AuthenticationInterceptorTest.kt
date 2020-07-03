package br.com.regmoraes.marvelcharacters.infrastructure.api

import br.com.regmoraes.marvelcharacters.infrastructure.api.AuthenticationInterceptor.generateHash
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class AuthenticationInterceptorTest {

    @Nested
    @DisplayName("Given a public and private key and a time stamp")
    inner class HashTest {

        private val timestamp = 1234L
        private val publicKey = "05c659e9d058b1e16a77d4c86a5eb42f"
        private val privateKey = "b142a2a84f7f0ad55b6f4ec0efb764a0ddba9ddb"

        @Test
        fun `The correct MD5 hash should be generated`() {

            val expectedHash = "99d37f81ea27e3a08a8d0db36c10df9b"

            assertEquals(expectedHash, generateHash(privateKey, publicKey, timestamp))
        }
    }

}