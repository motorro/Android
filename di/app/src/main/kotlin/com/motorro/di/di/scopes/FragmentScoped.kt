package com.motorro.di.di.scopes

import javax.inject.Scope

/**
 * Lives as long as Fragment lives
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentScoped