package ru.merionet.tasks.data

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Version value
 */
@JvmInline
@Serializable
value class Version(val value: String)

fun UUID.toVersion(): Version = Version(toString())
fun Version.toUUID(): UUID = UUID.fromString(value)
fun nextVersion(): Version = UUID.randomUUID().toVersion()
