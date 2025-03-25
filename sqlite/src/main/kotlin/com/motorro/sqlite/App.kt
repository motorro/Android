package com.motorro.sqlite

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment

class App : Application()

fun Activity.requireApp() = application as App
fun Fragment.requireApp() = requireActivity().requireApp()