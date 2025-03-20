package com.motorro.datastore.secure

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.motorro.datastore.data.MyPreferences
import kotlinx.serialization.json.Json

private const val KEY_PROVIDER = "AndroidKeyStore"
private const val KEY_ALIAS = "DATASTORE_DEMO"

/**
 * Kotlin JSON proto Data store for preferences with encryption
 */
val Context.secureDataStore: DataStore<MyPreferences> by dataStore(
    fileName = "securePrefs",
    serializer = SecurePreferencesKotlinSerializer(KEY_PROVIDER, KEY_ALIAS),
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { MyPreferences() }
    )
)

class SecurePreferencesKotlinSerializer(keyProvider: String, keyAlias: String) : SecureSerializer<MyPreferences>(keyProvider, keyAlias) {

    override val defaultValue: MyPreferences = MyPreferences()

    override fun decodeBytes(bytes: ByteArray): MyPreferences = try {
        Json.decodeFromString(
            MyPreferences.serializer(),
            bytes.decodeToString()
        )
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override fun encodeBytes(t: MyPreferences): ByteArray {
        return Json.encodeToString(MyPreferences.serializer(), t).encodeToByteArray()
    }
}
