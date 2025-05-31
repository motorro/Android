package com.motorro.architecture.country

import com.motorro.architecture.model.user.CountryCode
import java.util.Locale

/**
 * Country/country-code registry
 */
class CountryRegistry {

    private val countryCodes: Map<CountryCode, Locale> = buildMap {
        Locale.getISOCountries().forEach { country ->
            @Suppress("DEPRECATION")
            val locale = Locale("", country)
            put(CountryCode(locale.isO3Country.uppercase()), locale)
        }
    }

    /**
     * Returns country for the [iso3CountryCode]
     */
    fun getCountry(iso3CountryCode: CountryCode): String? {
        return countryCodes[CountryCode(iso3CountryCode.code?.uppercase())]?.displayName
    }

    /**
     * Gets a list of countries
     */
    fun getCountries(): List<Pair<CountryCode, String>> = countryCodes.entries
        .sortedBy { it.key.code }
        .map { (key, value) -> key to value.displayName }
}
