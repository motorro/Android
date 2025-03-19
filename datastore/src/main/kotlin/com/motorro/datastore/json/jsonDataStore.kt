package com.motorro.datastore.json

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.motorro.datastore.data.MyPreferences
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

/**
 * Kotlin JSON proto Data store for preferences
 */
val Context.jsonDataStore: DataStore<MyPreferences> by dataStore(
    fileName = "jsonPrefs",
    serializer = MyPreferencesKotlinSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { MyPreferences() }
    )
)

private object MyPreferencesKotlinSerializer : Serializer<MyPreferences> {
    override val defaultValue: MyPreferences = MyPreferences()

    override suspend fun readFrom(input: InputStream): MyPreferences {
        try {
            return Json.decodeFromString(
                MyPreferences.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MyPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(MyPreferences.serializer(), t).encodeToByteArray()
        )
    }
}