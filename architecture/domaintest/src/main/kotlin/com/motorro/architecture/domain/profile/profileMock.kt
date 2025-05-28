package com.motorro.architecture.domain.profile

import com.motorro.architecture.domain.session.USER_ID
import com.motorro.architecture.model.user.CountryCode
import com.motorro.architecture.model.user.NewUserProfile
import com.motorro.architecture.model.user.UserProfile

val PROFILE = UserProfile(
    id = USER_ID,
    displayName = "User",
    countryCode = CountryCode("RUS")
)

val NEW_PROFILE = NewUserProfile(
    displayName = "User",
    countryCode = CountryCode("RUS")
)