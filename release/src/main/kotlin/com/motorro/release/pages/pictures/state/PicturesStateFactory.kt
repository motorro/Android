package com.motorro.release.pages.pictures.state

import javax.inject.Inject

interface PicturesStateFactory {
    fun pictures(): PicturesState
    fun picturePreview(selected: Int): PicturesState

    class Impl @Inject constructor() : PicturesStateFactory {
        private val context = object : PicturesContext {
            override val factory: PicturesStateFactory = this@Impl
        }

        override fun pictures() = PictureList(context)
        override fun picturePreview(selected: Int) = PicturePreview(context, selected)
    }
}

