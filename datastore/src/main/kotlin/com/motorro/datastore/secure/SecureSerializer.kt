package com.motorro.datastore.secure

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.Serializer
import com.google.protobuf.kotlin.toByteString
import com.motorro.datastore.data.SecureProto
import com.motorro.datastore.data.secureProto
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Secure serializer
 * Encrypts data using a key stored in [keyProvider]
 * @param T Data type
 * @param keyProvider Key provider
 * @param keyAlias Key alias
 */
abstract class SecureSerializer<T: Any>(
    private val keyProvider: String,
    private val keyAlias: String
) : Serializer<T> {
    /**
     * Decodes bytes to T
     */
    protected abstract fun decodeBytes(bytes: ByteArray): T

    /**
     * Encodes T to bytes
     */
    protected abstract fun encodeBytes(t: T): ByteArray

    private val keyStore by lazy {
        KeyStore.getInstance(keyProvider).apply {
            load(null)
        }
    }

    final override suspend fun readFrom(input: InputStream): T {
        val encryptedData = SecureProto.parseFrom(input)
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance(KEY_TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, encryptedData.iv.toByteArray())
        )

        return decodeBytes(cipher.doFinal(encryptedData.data.toByteArray()))
    }

    final override suspend fun writeTo(t: T, output: OutputStream) {
        val serializedData = encodeBytes(t)
        val secretKey = getSecretKey()
        val cipher = Cipher.getInstance(KEY_TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKey
        )

        secureProto {
            iv = cipher.iv.toByteString()
            data = cipher.doFinal(serializedData).toByteString()
        }.writeTo(output)
    }

    private fun createKeyGenParameterSpec(keyAlias: String): KeyGenParameterSpec =
        KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_LENGTH)
            .build()

    @Synchronized
    private fun getSecretKey(): SecretKey {
        return if (keyStore.containsAlias(keyAlias)) {
            return keyStore.getKey(keyAlias, null) as SecretKey
        } else {
            createSecretKey()
        }
    }

    private fun createSecretKey(): SecretKey {
        val keyGenParameterSpec = createKeyGenParameterSpec(keyAlias)
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keyProvider).run {
            init(keyGenParameterSpec)
            generateKey()
        }
    }

    private companion object {
        const val KEY_LENGTH = 256
        const val KEY_TRANSFORMATION = "AES/GCM/NoPadding"
    }
}
