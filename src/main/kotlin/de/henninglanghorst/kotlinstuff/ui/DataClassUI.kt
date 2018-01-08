package de.henninglanghorst.kotlinstuff.ui

import de.henninglanghorst.kotlinstuff.ui.dsl.*
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.ERROR
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.scene.layout.Region
import javafx.stage.Stage
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction


abstract class DataClassUI(kClass: KClass<*>, definitions: FieldDefinitions = DefaultFieldDefinitions) : Application() {

    private val className = kClass.simpleName ?: "<unknown>"
    private val fieldDefinitions = definitions
    private val constructor = kClass.firstContructor
    private val constructorParameters = constructor.createConstructorParameters()

    override fun start(primaryStage: Stage) {
        primaryStage.init()
        primaryStage.show()
    }

    private fun Stage.init() {
        scene {
            borderPane {
                center {
                    gridPane {
                        hgap = 10.0
                        vgap = 10.0
                        forEachParameter { index, constructorParameter ->
                            label(0, index) {
                                text = constructorParameter.parameter.name ?: "$index"
                                minWidth = 150.0
                            }
                            control(index, 1) { fieldDefinitions.createControl(constructorParameter) }
                        }
                        button(3, 0) {
                            text = "OK"
                            minWidth = 50.0
                            onMouseClicked { _ -> createAndDisplayResult() }
                        }
                    }
                }
                padding = Insets(20.0)
            }
        }
        title = "Create new $className"
    }


    private fun forEachParameter(addParameterRow: (Int, ConstructorParameter) -> Unit) =
            constructorParameters.forEachIndexed(addParameterRow)

    private val <T : Any> KClass<T>.firstContructor get() = constructors.first { it.parameters.isNotEmpty() }
    private fun <T> KFunction<T>.createConstructorParameters() = parameters.map { ConstructorParameter(it) }
    private fun <T : Any> KFunction<T>.callFor(parameters: List<ConstructorParameter>) = call(*parameters.parameters)
    private val List<ConstructorParameter>.parameters get() = map { it.value }.toTypedArray()


    private fun createAndDisplayResult() {
        try {
            val obj = constructor.callFor(constructorParameters)
            showAlert(INFORMATION, "Created object $obj")
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            showAlert(ERROR, e.cause?.message)
        }
    }

    private fun showAlert(type: Alert.AlertType, message: String?) =
            Alert(type, message)
                    .apply { dialogPane.minHeight = Region.USE_PREF_SIZE }
                    .showAndWait()

}



