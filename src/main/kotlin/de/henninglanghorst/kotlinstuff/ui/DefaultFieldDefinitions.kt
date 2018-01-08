package de.henninglanghorst.kotlinstuff.ui

import de.henninglanghorst.kotlinstuff.ui.dsl.datePicker
import de.henninglanghorst.kotlinstuff.ui.dsl.fieldDefinition
import de.henninglanghorst.kotlinstuff.ui.dsl.listener
import de.henninglanghorst.kotlinstuff.ui.dsl.textField
import javafx.scene.control.Control
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import kotlin.reflect.full.createType

object DefaultFieldDefinitions : FieldDefinitions {

    private val nullableString = String::class.createType(nullable = true)
    private val nonNullString = String::class.createType(nullable = false)
    private val nullableLocalDate = LocalDate::class.createType(nullable = true)
    private val nullableInt = Int::class.createType(nullable = true)
    private val nullableBigInteger = BigInteger::class.createType(nullable = true)
    private val nullableBigDecimal = BigDecimal::class.createType(nullable = true)
    private val nullableLong = Long::class.createType(nullable = true)


    private val fieldDefinitions = listOf(
            fieldDefinition(nullableInt) {
                textField {
                    verifyConversion { it.toInt() }
                    listener { _, _, newValue -> value = newValue.toInt().takeIf { newValue.isNotEmpty() } }
                }
            },
            fieldDefinition(nullableLong) {
                textField {
                    verifyConversion { it.toLong() }
                    listener { _, _, newValue -> value = newValue.toLong().takeIf { newValue.isNotEmpty() } }
                }
            },
            fieldDefinition(nullableString) {
                textField {
                    listener { _, _, newValue -> value = newValue.takeIf { it.isNotEmpty() } }
                }
            },
            fieldDefinition(nonNullString) {
                textField {
                    listener { _, _, newValue -> value = newValue }
                }
            },
            fieldDefinition(nullableBigInteger) {
                textField {
                    verifyConversion { it.toBigInteger() }
                    listener { _, _, newValue -> value = newValue.toBigInteger().takeIf { newValue.isNotEmpty() } }
                }
            },
            fieldDefinition(nullableBigDecimal) {
                textField {
                    textField {
                        verifyConversion { it.toBigInteger() }
                        listener { _, _, newValue -> value = newValue.toBigDecimal().takeIf { newValue.isNotEmpty() } }
                    }
                }
            },
            fieldDefinition(nullableLocalDate) {
                datePicker {
                    listener { _, _, newValue -> value = newValue }
                }
            }
    )

    private fun TextField.verifyConversion(tryToConvert: (String) -> Unit) {
        textFormatter = TextFormatter<String> {
            try {
                tryToConvert(it.controlNewText)
                it
            } catch (e: NumberFormatException) {
                null
            }
        }
    }


    override fun createControl(constructorParameter: ConstructorParameter): Control {
        return fieldDefinitions.firstOrNull { it.type == constructorParameter.parameter.type }
                ?.createControl(constructorParameter)
                ?: Label("Unsupported type:  ${constructorParameter.parameter.type}")
    }

}