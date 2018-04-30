package de.henninglanghorst.kotlinstuff.http

import org.slf4j.Logger


fun Logger.debug(message: () -> String) {
    if (isDebugEnabled) {
        message().lineSequence().forEach { debug(it) }
    }
}

fun Logger.error(message: () -> String) {
    if (isErrorEnabled) {
        message().lineSequence().forEach { error(it) }
    }
}
