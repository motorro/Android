package com.motorro.release.pages.pictures.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Photo
import com.motorro.release.R

val pictures = listOf(
    Picture(
        vector = Icons.Filled.Photo,
        title = R.string.pic_photo
    ),
    Picture(
        vector = Icons.Filled.Attachment,
        title = R.string.p_attachment
    ),
    Picture(
        vector = Icons.Filled.AcUnit,
        title = R.string.p_conditioner
    ),
    Picture(
        vector = Icons.Filled.AccessAlarm,
        title = R.string.p_alarm
    ),
    Picture(
        vector = Icons.Filled.Factory,
        title = R.string.p_factory
    ),
    Picture(
        vector = Icons.Filled.AccountBalance,
        title = R.string.p_banc
    ),
    Picture(
        vector = Icons.Filled.AccountBalanceWallet,
        title = R.string.p_wallet
    ),
    Picture(
        vector = Icons.Filled.Album,
        title = R.string.p_album
    ),
    Picture(
        vector = Icons.Filled.Error,
        title = R.string.p_error
    )
)