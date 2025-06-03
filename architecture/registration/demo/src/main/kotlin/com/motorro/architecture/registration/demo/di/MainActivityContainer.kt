package com.motorro.architecture.registration.demo.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.ApplicationContainer
import com.motorro.architecture.appcore.di.ProvidesApplicationContainer

fun AppCompatActivity.buildContainer() = MainActivityContainer(
    activity = this,
    applicationContainer = (application as ProvidesApplicationContainer).applicationContainer
)

class MainActivityContainer(
    override val activity: AppCompatActivity,
    applicationContainer: ApplicationContainer
) : ActivityContainer, ApplicationContainer by applicationContainer {
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory = viewModelFactory {
        // No view-models needed
    }
}