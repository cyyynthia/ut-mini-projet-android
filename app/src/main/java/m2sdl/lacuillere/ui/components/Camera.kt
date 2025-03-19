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
					.aspectRatio(3f / 4f)
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
			onClick = { viewModel.takePicture(ctx, ambientLight); println(ambientLight) },
		) {}
	}
}
