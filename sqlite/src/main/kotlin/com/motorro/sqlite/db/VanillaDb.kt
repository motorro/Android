package com.motorro.sqlite.db

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.core.content.contentValuesOf
import com.motorro.sqlite.data.Image
import com.motorro.sqlite.data.ListImage
import com.motorro.sqlite.data.PhotoFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

object PhotoContract {
    const val DATABASE_NAME = "photo.db"
    const val DATABASE_VERSION = 1

    object Photo {
        const val TABLE_NAME = "photo"
        const val COLUMN_PATH = "path"
        const val COLUMN_NAME = "name"
        const val COLUMN_CREATED = "created"
    }

    private const val CREATE_PHOTO = """
        CREATE TABLE ${Photo.TABLE_NAME} (
            ${Photo.COLUMN_PATH} TEXT NOT NULL PRIMARY KEY,
            ${Photo.COLUMN_NAME} TEXT NOT NULL,
            ${Photo.COLUMN_CREATED} TEXT NOT NULL
        )
    """

    fun helper(context: Context): SQLiteOpenHelper = object : SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {
        override fun onCreate(db: android.database.sqlite.SQLiteDatabase) {
            db.execSQL(CREATE_PHOTO)
        }

        override fun onUpgrade(db: android.database.sqlite.SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Not implemented
        }
    }
}

class PhotoDbImpl(context: Context): PhotoDb {

    private val helper = PhotoContract.helper(context)

    override fun getList(filter: PhotoFilter): Flow<List<ListImage>> = flow {
        val db = helper.readableDatabase
        val projection = arrayOf(
            PhotoContract.Photo.COLUMN_PATH,
            PhotoContract.Photo.COLUMN_NAME,
            PhotoContract.Photo.COLUMN_CREATED
        )

        var selection: String? = null
        var selectionArgs: Array<String>? = null

        val nameToSearch = filter.name
        if (nameToSearch != null) {
            selection = "${PhotoContract.Photo.COLUMN_NAME} LIKE ?"
            selectionArgs = arrayOf("%$nameToSearch%")
        }

        val sortOrder = "${PhotoContract.Photo.COLUMN_CREATED} DESC"

        val cursor = db.query(
            PhotoContract.Photo.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        cursor.use {
            val list = mutableListOf<ListImage>()
            while (cursor.moveToNext()) {
                list.add(ListImage(
                    path = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(PhotoContract.Photo.COLUMN_PATH))),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(PhotoContract.Photo.COLUMN_NAME)),
                    dateTimeTaken = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(PhotoContract.Photo.COLUMN_CREATED)))
                ))
            }
            emit(list)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addImage(image: Image)  {
        withContext(Dispatchers.IO) {
            val db = helper.writableDatabase
            db.insert(PhotoContract.Photo.TABLE_NAME, null, contentValuesOf(
                PhotoContract.Photo.COLUMN_PATH to image.path.toString(),
                PhotoContract.Photo.COLUMN_NAME to image.name,
                PhotoContract.Photo.COLUMN_CREATED to image.dateTimeTaken.toString()
            ))
        }
    }

    override suspend fun deleteImage(imagePath: Uri) {
        withContext(Dispatchers.IO) {
            val db = helper.writableDatabase
            db.delete(PhotoContract.Photo.TABLE_NAME, "${PhotoContract.Photo.COLUMN_PATH} = ?", arrayOf(imagePath.toString()))
        }
    }
}