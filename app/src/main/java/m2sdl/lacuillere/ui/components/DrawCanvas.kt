/*!
 * Original work's copyright notice
 * ================================
 * Copyright (c) 2023 Smart Tool Factory
 * Released under the MIT license
 * SPDX-License-Identifier: MIT
 * https://github.com/SmartToolFactory/Compose-Drawing-App
 * =================================
 * Modified to fit the project's needs.
 * Adapted to Material 3.
 */

package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.positionChange
import m2sdl.lacuillere.gesture.dragMotionEvent
import m2sdl.lacuillere.viewmodel.CanvasViewModel

@Composable
fun DrawCanvas(bitmap: ImageBitmap, model: CanvasViewModel) {
	val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

	val drawModifier = Modifier
		.fillMaxWidth()
		.aspectRatio(aspectRatio)
		.background(Color.White)
		.dragMotionEvent(
			onDragStart = { pointerInputChange ->
				model.dragStart(pointerInputChange)
				if (pointerInputChange.pressed != pointerInputChange.previousPressed) pointerInputChange.consume()
			},
			onDrag = { pointerInputChange ->
				model.drag(pointerInputChange)
				if (pointerInputChange.positionChange() != Offset.Zero) pointerInputChange.consume()
			},
			onDragEnd = { pointerInputChange ->
				model.dragEnd()
				if (pointerInputChange.pressed != pointerInputChange.previousPressed) pointerInputChange.consume()
			}
		)

	Canvas(modifier = drawModifier) {
		// at this point I don't really care anymore if it's gross and hacky
		// I should actually keep the paths as relative rather than absolute but yeah idc
		// I don't get paid enough for this shit
		model.canvasSize = drawContext.size

		model.tickMotionEvent()
		model.draw(this, bitmap)
	}
}
