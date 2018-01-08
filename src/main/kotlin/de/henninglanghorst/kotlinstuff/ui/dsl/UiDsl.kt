package de.henninglanghorst.kotlinstuff.ui.dsl

import de.henninglanghorst.kotlinstuff.ui.ConstructorParameter
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.time.LocalDate
import kotlin.reflect.KType


data class FieldDefinition(val type: KType, val control: ConstructorParameter.() -> Control) {
    fun createControl(constructorParameter: ConstructorParameter) = constructorParameter.control()
}


fun fieldDefinition(type: KType, control: ConstructorParameter.() -> Control) = FieldDefinition(type, control)

fun textField(init: TextField.() -> Unit) = TextField().apply(init)
fun datePicker(init: DatePicker.() -> Unit) = DatePicker().apply(init)
fun label(init: Label.() -> Unit) = Label().apply(init)
fun gridPane(init: GridPane.() -> Unit) = GridPane().apply(init)
fun borderPane(init: BorderPane.() -> Unit) = BorderPane().apply(init)

fun DatePicker.listener(function: (ObservableValue<out LocalDate>, LocalDate?, LocalDate?) -> Unit) =
        valueProperty().addListener(function)

fun TextInputControl.listener(function: (ObservableValue<out String>, String, String) -> Unit) =
        textProperty().addListener(function)

fun Button.onMouseClicked(function: (MouseEvent) -> Unit) {
    onMouseClicked = EventHandler(function)
}

fun Button.onMouseClicked(function: () -> Unit) {
    onMouseClicked = EventHandler { _ -> function() }
}

fun Stage.scene(borderPane1: () -> BorderPane) {
    scene = Scene(borderPane1())
}


fun BorderPane.center(createCenter: () -> Node) {
    center = createCenter()
}

fun GridPane.button(columnIndex: Int, rowIndex: Int, init: Button.() -> Unit) =
        add(Button().apply(init), columnIndex, rowIndex)

fun GridPane.control(rowIndex: Int, columnIndex: Int, createControl: () -> Control) =
        add(createControl(), columnIndex, rowIndex)

fun GridPane.label(columnIndex: Int, rowIndex: Int, init: Label.() -> Unit) =
        add(Label().apply(init), columnIndex, rowIndex)

