package org.it.business.domain.valueobject

import java.util.*

/**
 * Value object representing a locale
 */
data class Locale private constructor(
    val language: String,
    val country: String = "",
    val variant: String = ""
) {
    
    companion object {
        fun of(language: String, country: String = "", variant: String = ""): Locale {
            return Locale(language.lowercase(), country.uppercase(), variant)
        }
        
        fun fromJavaLocale(javaLocale: java.util.Locale): Locale {
            return Locale(javaLocale.language, javaLocale.country, javaLocale.variant)
        }
        
        val ENGLISH = Locale("en")
        val SPANISH = Locale("es")
        val FRENCH = Locale("fr")
    }
    
    fun toJavaLocale(): java.util.Locale {
        return java.util.Locale(language, country, variant)
    }
    
    override fun toString(): String {
        return buildString {
            append(language)
            if (country.isNotEmpty()) {
                append("_").append(country)
            }
            if (variant.isNotEmpty()) {
                append("_").append(variant)
            }
        }
    }
}
