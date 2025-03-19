package com.motorro.datastore.proto

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.motorro.datastore.data.MyPreferences
import com.motorro.datastore.data.MyPreferencesProto
import com.motorro.datastore.data.myPreferencesProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import java.io.InputStream
import java.io.OutputStream

/**
 * Proto Data store for preferences DTO
 */
private val Context.protoProtoDataStore: DataStore<MyPreferencesProto> by dataStore(
    fileName = "protoPrefs",
    serializer = MyPreferencesProtoSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { MyPreferencesProto.getDefaultInstance() }
    )
)

/**
 * Proto Data store for preferences
 * Maps proto DTO to domain model
 */
val Context.protoDataStore: DataStore<MyPreferences> get() = object: DataStore<MyPreferences> {
    override val data: Flow<MyPreferences> = protoProtoDataStore.data.map {
        it.toMyPreferences()
    }

    override suspend fun updateData(transform: suspend (t: MyPreferences) -> MyPreferences): MyPreferences {
        return protoProtoDataStore.updateData { proto ->
            transform(proto.toMyPreferences()).let { prefs ->
                myPreferencesProto {
                    userName = prefs.userName
                    password = prefs.password
                    if (null != prefs.dob) {
                        dob = prefs.dob.toEpochDays()
                    } else {
                        clearDob()
                    }
                    displayDob = prefs.displayDob
                }
            }
        }.toMyPreferences()
    }

    private fun MyPreferencesProto.toMyPreferences(): MyPreferences = MyPreferences(
        userName = userName,
        password = password,
        dob = if (hasDob()) LocalDate.fromEpochDays(dob) else null,
        displayDob = displayDob
    )
}


private object MyPreferencesProtoSerializer : Serializer<MyPreferencesProto> {
    override val defaultValue: MyPreferencesProto = MyPreferencesProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MyPreferencesProto {
        try {
            return MyPreferencesProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MyPreferencesProto, output: OutputStream) {
        t.writeTo(output)
    }
}