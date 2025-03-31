package com.motorro.sqlite

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.db.PhotoDb
import com.motorro.sqlite.db.TagsDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import java.io.File
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class AddImageViewModel(
    private val sourceImage: Uri,
    application: Application,
    private val clock: Clock,
    private val db: PhotoDb,
    private val tagsDb: TagsDb
) : AndroidViewModel(application) {

    private val _image: MutableStateFlow<Uri> = MutableStateFlow(sourceImage)
    val image: StateFlow<Uri?> get() = _image.asStateFlow()

    private val _title: MutableStateFlow<String> = MutableStateFlow("")
    val title: StateFlow<String> get() = _title.asStateFlow()

    fun setTitle(title: String) {
        _title.value = title
    }

    private val _tags: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())
    val tags: StateFlow<List<ListTag>> = _tags
        .flatMapLatest { if (it.isEmpty()) flowOf(emptyList()) else tagsDb.getList(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addTag(tagId: Int) {
        _tags.value += tagId
    }
    fun removeTag(tagId: Int) {
        _tags.value -= tagId
    }

    private val _dateTaken: MutableStateFlow<LocalDateTime?> = MutableStateFlow(null)
    val dateTaken: StateFlow<LocalDateTime?> get() = _dateTaken.asStateFlow()

    val saveEnabled = title
        .map { it.isNotBlank() }
        .combine(dateTaken.map { it != null }) { title, date -> title && date }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _complete = MutableStateFlow(false)
    val complete: StateFlow<Boolean> get() = _complete.asStateFlow()

    init {
        viewModelScope.launch {
            _title.value = getFileName(sourceImage)
            _dateTaken.value = getImageTaken(sourceImage)
        }
    }

    fun save() = viewModelScope.launch(Dispatchers.IO) {

        val image = saveImage(sourceImage)
        val name = title.value
        val dateTaken = getImageTaken(sourceImage)

        val tags = tagsDb.getList(_tags.value)
            .first()
            .map { it.id }
            .toSet()

        db.addImage(Image(image, name, dateTaken), tags)

        _complete.value = true
    }

    private suspend fun saveImage(image: Uri): Uri = withContext(Dispatchers.IO) {
        val context: Context = getApplication()
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        val type = context.contentResolver.getType(image) ?: "image/jpeg"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type) ?: "jpg"
        val file = File.createTempFile("img", ".$extension", dir)

        context.contentResolver.openInputStream(image).use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        return@withContext Uri.fromFile(file)
    }

    private suspend fun getFileName(image: Uri): String = withContext(Dispatchers.IO) {
        val context: Context = getApplication()
        context.contentResolver.query(image, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                return@withContext  cursor.getString(nameIndex)
            }
        }
        return@withContext ""
    }

    private suspend fun getImageTaken(image: Uri): LocalDateTime = withContext(Dispatchers.IO) {
        val context: Context = getApplication()
        val exifDate = context.contentResolver.openInputStream(image).use {
            it?.let {
                ExifInterface(it).getAttribute(ExifInterface.TAG_DATETIME)?.let {
                    exifDateTimeFormat.parse(it)
                }
            }
        }
        return@withContext exifDate ?: clock.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    private companion object {
        val exifDateTimeFormat = LocalDateTime.Format {
            year(Padding.ZERO)
            char(':')
            monthNumber(Padding.ZERO)
            char(':')
            day(Padding.ZERO)
            char(' ')
            hour(Padding.ZERO)
            char(':')
            minute(Padding.ZERO)
            char(':')
            second(Padding.ZERO)
        }
    }

    class Factory(private val app: App, private val image: Uri): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddImageViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddImageViewModel(image, app, Clock.System, app.db, app.db.tagsDb) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}