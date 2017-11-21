package de.henninglanghorst.kotlinstuff.ui


import de.henninglanghorst.kotlinstuff.ui.KTypes.nullableInt
import de.henninglanghorst.kotlinstuff.ui.KTypes.nullableLocalDate
import de.henninglanghorst.kotlinstuff.ui.KTypes.nullableLong
import de.henninglanghorst.kotlinstuff.ui.KTypes.nullableString
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.time.LocalDate
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties


fun main(args: Array<String>) {

    Application.launch(BeanUI::class.java, *args)
}

class BeanUI : Application() {
    override fun start(primaryStage: Stage) {
        primaryStage.apply {

            val borderPane = BorderPane(gridPane(Person())).apply { padding = Insets(20.0) }
            scene = Scene(borderPane)
            title = "Bean UI"
        }.show()
    }

    private fun <T : Any> gridPane(obj: T) =
            GridPane().apply {
                hgap = 10.0
                vgap = 10.0

                val mutableProperties = obj::class.mutableProperties
                mutableProperties.forEachIndexed { index, property ->
                    add(Label(property.name).apply { minWidth = 150.0 }, 0, index)
                    obj.controlFor(property)?.also { add(it, 1, index) }
                }

                add(Button("OK").apply { onMouseClicked = EventHandler { _ -> println("Object: $obj") } }, 3, 0)


            }

    @Suppress("UNCHECKED_CAST")
    private val <T : Any> KClass<out T>.mutableProperties
        get() = memberProperties.filter { it is KMutableProperty1 }.map { it as KMutableProperty1<T, *> }


    private fun <T : Any> T.controlFor(property: KMutableProperty1<T, *>) = when {
        property.returnType == nullableInt ->
            TextField().apply {
                textFormatter = TextFormatter<String> { it.takeIfConversionSuccessful { controlNewText.toInt() } }
                textProperty().addListener { _, _, newValue ->
                    property.asPropertyOf<T, Int?>()
                            .set(this@controlFor, newValue.toInt().takeIf { newValue.isNotEmpty() })
                }
            }
        property.returnType == nullableLong ->
            TextField().apply {
                textFormatter = TextFormatter<String> { it.takeIfConversionSuccessful { controlNewText.toLong() } }
                textProperty().addListener { _, _, newValue ->
                    property.asPropertyOf<T, Long?>()
                            .set(this@controlFor, newValue.toLong().takeIf { newValue.isNotEmpty() })
                }
            }
        property.returnType == nullableString ->
            TextField().apply {
                textProperty().addListener { _, _, newValue ->
                    property.asPropertyOf<T, String?>()
                            .set(this@controlFor, newValue.takeIf { it.isNotEmpty() })
                }
            }
        property.returnType == KTypes.nonNullString ->
            TextField().apply {
                textProperty().addListener { _, _, newValue ->
                    property.asPropertyOf<T, String>()
                            .set(this@controlFor, newValue)
                }
            }
        property.returnType == nullableLocalDate -> DatePicker()
                .apply {
                    valueProperty().addListener { _, _, newValue ->
                        property.asPropertyOf<T, LocalDate?>()
                                .set(this@controlFor, newValue)
                    }


                }
        else -> null
    }


    private fun <T> T.takeIfConversionSuccessful(test: T.() -> Unit) =
            try {
                test()
                this
            } catch (e: NumberFormatException) {
                null
            }

    @Suppress("UNCHECKED_CAST")
    private inline fun <T : Any, reified R> KMutableProperty1<T, *>.asPropertyOf() = this as KMutableProperty1<T, R>


}


object KTypes {
    val nullableString = String::class.createType(nullable = true)
    val nonNullString = String::class.createType(nullable = false)
    val nullableLocalDate = LocalDate::class.createType(nullable = true)
    val nullableInt = Int::class.createType(nullable = true)
    val nullableLong = Long::class.createType(nullable = true)

}


data class Person(
        var firstName: String = "",
        var lastName: String = "",
        var birthName: String? = null,
        var birthday: LocalDate? = null
)

