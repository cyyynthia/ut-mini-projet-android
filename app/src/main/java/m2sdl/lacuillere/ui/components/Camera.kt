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
 */

package m2sdl.lacuillere.ui.components

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import m2sdl.lacuillere.viewmodel.CameraViewModel

@Composable
fun CameraPreview(
	viewModel: CameraViewModel,
	lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
	val surfaceRequest by viewModel.surfaceRequest
	val context = LocalContext.current

	val aspectRatio by viewModel.cameraAspectRatio

	LaunchedEffect(lifecycleOwner) {
		viewModel.startCamera(context.applicationContext, lifecycleOwner)
	}

	surfaceRequest?.let { request ->
		Column(
			Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Box(
				Modifier
					.aspectRatio(aspectRatio)
					.fillMaxSize()
			) {
				CameraXViewfinder(
					surfaceRequest = request,
				)
			}
		}
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShutterButton(viewModel: CameraViewModel, ambientLight: Float) {
	val ctx = LocalContext.current
	val ambientLight by rememberUpdatedState(ambientLight)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.absoluteOffset(0.dp, 0.dp)
			.windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
			.padding(bottom = 16.dp),
		verticalArrangement = Arrangement.Bottom,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Button(
			modifier = Modifier
				.width(96.dp)
				.height(96.dp),
			elevation = ButtonDefaults.buttonElevation(2.dp),
			colors = ButtonDefaults.buttonColors(containerColor = Color.White),
			shape = RoundedCornerShape(48.dp),
			onClick = { viewModel.takePicture(ctx, ambientLight) },
		) {}
	}
}
