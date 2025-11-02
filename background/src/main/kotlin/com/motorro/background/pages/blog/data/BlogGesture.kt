package com.motorro.background.pages.blog.data

sealed class BlogGesture {
    data object Refresh : BlogGesture()
}