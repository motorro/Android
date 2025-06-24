package com.motorro.di.di.scopes

import javax.inject.Scope

/**
 * Lives as long as Activity lives
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScoped