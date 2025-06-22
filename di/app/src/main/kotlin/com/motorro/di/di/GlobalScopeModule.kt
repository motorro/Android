package com.motorro.di.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Module to provide global scope
 */
@Module
class GlobalScopeModule {
    /**
     * Provides global scope
     */
    @Provides
    @OptIn(DelicateCoroutinesApi::class)
    fun scope(): CoroutineScope = GlobalScope
}