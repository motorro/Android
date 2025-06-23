package com.motorro.di.di.scopes

import javax.inject.Scope

/**
 * Lives as long as Application lives
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScoped