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

package m2sdl.lacuillere.gesture

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange

suspend fun AwaitPointerEventScope.awaitDragMotionEvent(
	onDragStart: (PointerInputChange) -> Unit = {},
	onDrag: (PointerInputChange) -> Unit = {},
	onDragEnd: (PointerInputChange) -> Unit = {},
) {
	// Wait for at least one pointer to press down, and set first contact position
	val down: PointerInputChange = awaitFirstDown()
	onDragStart(down)

	var pointer = down

	// 🔥 Waits for drag threshold to be passed by pointer
	// or it returns null if up event is triggered
	val change: PointerInputChange? =
		awaitTouchSlopOrCancellation(down.id) { change: PointerInputChange, over: Offset ->
			// 🔥🔥 If consumePositionChange() is not consumed drag does not
			// function properly.
			// Consuming position change causes change.positionChanged() to return false.
			if (change.positionChange() != Offset.Zero) change.consume()
		}

	if (change != null) {
		// 🔥 Calls  awaitDragOrCancellation(pointer) in a while loop
		drag(change.id) { pointerInputChange: PointerInputChange ->
			pointer = pointerInputChange
			onDrag(pointer)
		}

		// All of the pointers are up
		onDragEnd(pointer)
	} else {
		// Drag threshold is not passed and last pointer is up
		onDragEnd(pointer)
	}
}

fun Modifier.dragMotionEvent(
	onDragStart: (PointerInputChange) -> Unit = {},
	onDrag: (PointerInputChange) -> Unit = {},
	onDragEnd: (PointerInputChange) -> Unit = {},
) = this.then(
	Modifier.pointerInput(Unit) {
		awaitEachGesture {
			awaitDragMotionEvent(onDragStart, onDrag, onDragEnd)
		}
	}
)
