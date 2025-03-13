package com.motorro.filestore

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageWithComment(val file: Uri, val comment: String): Parcelable