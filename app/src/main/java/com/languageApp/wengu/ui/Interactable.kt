package com.languageApp.wengu.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

abstract class Interactable {
    abstract val function : () -> Unit
    abstract val label : String
    abstract val color : Color
    abstract val textColor : Color
    abstract val onColor : Color
    abstract val onTextColor : Color
}
data class InteractableIcon constructor(
    override val function : () -> Unit,
    override val label : String,
    override val color : Color,
    override val textColor : Color,
    override val onColor : Color = textColor,
    override val onTextColor : Color = color,
    val icon : ImageVector,
    ) : Interactable()
data class InteractableText constructor(
    override val function : () -> Unit,
    override val label : String,
    override val color : Color,
    override val textColor : Color,
    override val onColor : Color = textColor,
    override val onTextColor : Color = color,
) : Interactable()
