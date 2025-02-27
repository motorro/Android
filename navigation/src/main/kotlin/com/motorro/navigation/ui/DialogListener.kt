package com.motorro.navigation.ui

interface DialogListener {
    fun onDialogConfirm(tag: String) = Unit
    fun onDialogDismiss(tag: String) = Unit
}