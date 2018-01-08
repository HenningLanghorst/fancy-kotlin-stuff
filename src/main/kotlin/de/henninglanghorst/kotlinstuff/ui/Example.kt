package de.henninglanghorst.kotlinstuff.ui

import javafx.application.Application
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate


fun main(args: Array<String>) {
    Application.launch(PersonUI::class.java, *args)

}

class PersonUI : DataClassUI(Person::class, DefaultFieldDefinitions)


data class Person(
        val firstName: String = "",
        val lastName: String = "",
        val birthName: String? = null,
        val birthday: LocalDate? = null,
        val someInt: Int? = null,
        val someLong: Long? = null,
        val someBigInteger: BigInteger? = null,
        val someBigDecimal: BigDecimal? = null
)
