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
 * Adapted to Material 3.
 */

package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import m2sdl.lacuillere.gesture.dragMotionEvent
import m2sdl.lacuillere.viewmodel.CanvasViewModel

@Composable
fun DrawCanvas(bitmap: ImageBitmap, model: CanvasViewModel) {
	val ctx = LocalContext.current
	val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

	val drawModifier = Modifier
		.fillMaxWidth()
		.aspectRatio(aspectRatio)
		.background(Color.White)
		.dragMotionEvent(
			onDragStart = { pointerInputChange -> model.dragStart(ctx, pointerInputChange) },
			onDrag = { pointerInputChange -> model.drag(pointerInputChange) },
			onDragEnd = { pointerInputChange -> model.dragEnd(pointerInputChange) }
		)

	Canvas(modifier = drawModifier) {
		// at this point I don't really care anymore if it's gross and hacky
		// I should actually keep the paths as relative rather than absolute but yeah idc
		// I don't get paid enough for this shit
		model.canvasSize = drawContext.size

		model.tickMotionEvent()
		model.draw(ctx, this, bitmap)
	}
}
