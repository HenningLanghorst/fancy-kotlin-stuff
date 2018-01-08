package de.henninglanghorst.kotlinstuff.ui

import kotlin.reflect.KParameter

data class ConstructorParameter(
        val parameter: KParameter,
        var value: Any? = null
)