/*!
 * Copyright (c) Anton Haehn, Cynthia Rey, All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ================================
 * Original work's copyright notice
 * ================================
 * Copyright (c) 2023 Smart Tool Factory
 * Released under the MIT license
 * SPDX-License-Identifier: MIT
 * https://github.com/SmartToolFactory/Compose-Drawing-App
 * =================================
 * Modified to fit the project's needs.
 */

package m2sdl.lacuillere.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntSize
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import m2sdl.lacuillere.data.PathProperties
import m2sdl.lacuillere.gesture.MotionEvent
import m2sdl.lacuillere.resources.Sticker
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class CanvasViewModel : ViewModel() {
	private val rng = Random()

	/**
	 * Draw mode or touch mode
	 */
	private val _drawMode = mutableStateOf(DrawMode.Draw)
	val drawMode: State<DrawMode> = _drawMode

	/**
	 * Paths that are added, this is required to have paths with different options and paths
	 *  ith erase to keep over each other
	 */
	private val paths = mutableStateListOf<Pair<Path, PathProperties>>()

	/**
	 * Paths that are undone via button. These paths are restored if user pushes
	 * redo button if there is no new path drawn.
	 *
	 * If new path is drawn after this list is cleared to not break paths after undoing previous
	 * ones.
	 */
	private val pathsUndone = mutableStateListOf<Pair<Path, PathProperties>>()

	/**
	 * Canvas touch state. [MotionEvent.Idle] by default, [MotionEvent.Down] at first contact,
	 * [MotionEvent.Move] while dragging and [MotionEvent.Up] when first pointer is up
	 */
	private val _motionEventState = mutableStateOf(MotionEvent.Idle)
	private var motionEvent by _motionEventState

	/**
	 * Current position of the pointer that is pressed or being moved
	 */
	private val _currentPositionState = mutableStateOf(Offset.Unspecified)
	private var currentPosition by _currentPositionState

	/**
	 * Previous motion event before next touch is saved into this current position.
	 */
	private val _previousPositionState = mutableStateOf(Offset.Unspecified)
	private var previousPosition by _previousPositionState

	/**
	 * Path that is being drawn between [MotionEvent.Down] and [MotionEvent.Up]. When
	 * pointer is up this path is saved to **paths** and new instance is created
	 */
	private val _currentPathState = mutableStateOf(Path())
	private var currentPath by _currentPathState

	/**
	 * Properties of path that is currently being drawn between
	 * [MotionEvent.Down] and [MotionEvent.Up].
	 */
	val currentPathProperty = mutableStateOf(PathProperties())

	private val stickers = mutableStateListOf<Triple<Sticker, Offset, Float>>()
	val selectedSticker = mutableStateOf<Sticker>(Sticker.HeartEyes)

	var canvasSize: Size = Size(300f, 400f)


	fun tickMotionEvent() {
		when (motionEvent) {
			MotionEvent.Down -> {
				if (_drawMode.value != DrawMode.Move) {
					currentPath.moveTo(currentPosition.x, currentPosition.y)
				}

				previousPosition = currentPosition

			}

			MotionEvent.Move -> {
				if (_drawMode.value != DrawMode.Move) {
					currentPath.quadraticTo(
						previousPosition.x,
						previousPosition.y,
						(previousPosition.x + currentPosition.x) / 2,
						(previousPosition.y + currentPosition.y) / 2
					)
				}

				previousPosition = currentPosition
			}

			MotionEvent.Up -> {
				if (_drawMode.value != DrawMode.Move) {
					currentPath.lineTo(currentPosition.x, currentPosition.y)

					// Pointer is up save current path
					// paths[currentPath] = currentPathProperty
					paths.add(Pair(currentPath, currentPathProperty.value))

					// Since paths are keys for map, use new one for each key
					// and have separate path for each down-move-up gesture cycle
					currentPath = Path()
				}

				// Since new path is drawn no need to store paths to undone
				pathsUndone.clear()

				// If we leave this state at MotionEvent.Up it causes current path to draw
				// line from (0,0) if this composable recomposes when draw mode is changed
				currentPosition = Offset.Unspecified
				motionEvent = MotionEvent.Idle
				previousPosition = currentPosition
			}

			else -> Unit
		}
	}

	fun dragStart(ctx: Context, event: PointerInputChange) {
		if (event.pressed != event.previousPressed) event.consume()
		if (_drawMode.value == DrawMode.Idle) return
		if (_drawMode.value == DrawMode.Sticker) return applyOrRemoveSticker(ctx, event)

		motionEvent = MotionEvent.Down
		currentPosition = event.position
	}

	fun drag(event: PointerInputChange) {
		if (event.positionChange() != Offset.Zero) event.consume()
		if (_drawMode.value == DrawMode.Idle || _drawMode.value == DrawMode.Sticker) return

		motionEvent = MotionEvent.Move
		currentPosition = event.position

		if (_drawMode.value == DrawMode.Move) {
			val change = event.positionChange()
			paths.forEach { entry ->
				val path: Path = entry.first
				path.translate(change)
			}

			currentPath.translate(change)
		}
	}

	fun dragEnd(event: PointerInputChange) {
		if (event.pressed != event.previousPressed) event.consume()
		if (_drawMode.value == DrawMode.Idle || _drawMode.value == DrawMode.Sticker) return

		motionEvent = MotionEvent.Up
	}

	fun undo() {
		if (paths.isNotEmpty()) {
			val lastItem = paths.last()
			val lastPath = lastItem.first
			val lastPathProperty = lastItem.second

			paths.remove(lastItem)
			pathsUndone.add(Pair(lastPath, lastPathProperty))
		}
	}

	fun redo() {
		if (pathsUndone.isNotEmpty()) {
			val lastPath = pathsUndone.last().first
			val lastPathProperty = pathsUndone.last().second

			pathsUndone.removeAt(pathsUndone.lastIndex)
			paths.add(Pair(lastPath, lastPathProperty))
		}
	}

	fun setDrawMode(drawMode: DrawMode) {
		motionEvent = MotionEvent.Idle
		_drawMode.value = drawMode
	}

	fun clear() {
		motionEvent = MotionEvent.Idle
		_drawMode.value = DrawMode.Draw
		paths.clear()
		pathsUndone.clear()
		stickers.clear()

		currentPath = Path()
		currentPathProperty.value = PathProperties()
	}

	fun draw(ctx: Context, scope: DrawScope, image: ImageBitmap) {
		with(scope.drawContext.canvas.nativeCanvas) {
			val checkPoint = saveLayer(null, null)

			scope.drawImage(image, dstSize = scope.drawContext.size.toIntSize())
			scope.draw0(ctx)

			if (motionEvent != MotionEvent.Idle) {
				scope.drawPath(
					color = currentPathProperty.value.color,
					path = currentPath,
					style = Stroke(
						width = currentPathProperty.value.strokeWidth,
						cap = currentPathProperty.value.strokeCap,
						join = currentPathProperty.value.strokeJoin
					)
				)
			}

			restoreToCount(checkPoint)
		}
	}

	fun draw(ctx: Context, bitmap: ImageBitmap): ImageBitmap {
		val drawScope = CanvasDrawScope()
		val size = canvasSize.toIntSize()

		val finalImage = ImageBitmap(size.width, size.height)

		drawScope.draw(
			density = Density(ctx),
			layoutDirection = LayoutDirection.Ltr,
			canvas = Canvas(finalImage),
			size = canvasSize,
		) {
			draw(ctx, this, bitmap)
		}

		return finalImage
	}

	private fun DrawScope.draw0(ctx: Context) {
		val stickerSize = 72.dp.roundToPx()
		val stickerCenter = IntOffset(stickerSize / 2, stickerSize / 2)

		paths.forEach { drawPath(it.first, it.second) }
		stickers.forEach {
			val stickerImage = ctx.resources.getDrawable(it.first.resource, null)
				.toBitmap(stickerSize, stickerSize)
				.asImageBitmap()

			val position = it.second.round().minus(stickerCenter)

			withTransform({ rotate(degrees = it.third, pivot = it.second) }) {
				drawImage(stickerImage, dstOffset = position)
			}
		}

		if (motionEvent != MotionEvent.Idle) {
			drawPath(currentPath, currentPathProperty.value)
		}
	}

	private fun DrawScope.drawPath(path: Path, properties: PathProperties) {
		drawPath(
			color = properties.color,
			alpha = properties.alpha,
			path = path,
			style = Stroke(
				width = properties.strokeWidth,
				cap = properties.strokeCap,
				join = properties.strokeJoin
			)
		)
	}

	private fun applyOrRemoveSticker(ctx: Context, event: PointerInputChange) {
		val stickerSize = Density(ctx).run { 72.dp.roundToPx() }
		val threshold = stickerSize / 2f

		val removed = stickers.removeIf {
			val diff = it.second.minus(event.position)
			val dst = sqrt(diff.x.pow(2) + diff.y.pow(2))
			dst <= threshold
		}

		if (!removed) {
			val rotateMax = 20f

			stickers.add(
				Triple(
					selectedSticker.value,
					event.position,
					(rng.nextFloat() * rotateMax * 2) - rotateMax
				)
			)
		}
	}

	enum class DrawMode {
		Idle,
		Draw,
		Move,
		Sticker,
	}
}
