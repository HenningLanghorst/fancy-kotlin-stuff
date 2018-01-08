package de.henninglanghorst.kotlinstuff.ui

import javafx.scene.control.Control

interface FieldDefinitions {
    fun createControl(constructorParameter: ConstructorParameter): Control
}