package m2sdl.lacuillere

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import m2sdl.lacuillere.ui.components.CameraPreview
import m2sdl.lacuillere.ui.components.ShutterButton
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import m2sdl.lacuillere.viewmodel.CameraViewModel

class PhotoActivity : ComponentActivity() {
	private val permissionGranted = mutableStateOf(false)

	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		val insetsController = WindowInsetsControllerCompat(window, window.decorView)
		insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

		setContent {
			val density = LocalDensity.current
			val permissionGranted by permissionGranted

			val cameraViewModel: CameraViewModel by viewModels()
			val activityTerminated by cameraViewModel.activityTerminated
			val processing by cameraViewModel.processing
			val image by cameraViewModel.image

			var isApplyingFilters by remember { mutableStateOf(false) }

			RequestCamera()

			LaunchedEffect(activityTerminated) {
				if (activityTerminated) finish()
			}

			LaunchedEffect(image) {
				val type = WindowInsetsCompat.Type.systemBars()
				if (image == null) {
					insetsController.hide(type)
				} else {
					insetsController.show(type)
				}
			}

			LaCuillereTheme {
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					topBar = {
						AnimatedVisibility(
							visible = image != null,
							enter = slideInVertically { with(density) { -64.dp.roundToPx() } } + fadeIn(),
							exit = slideOutVertically { with(density) { -64.dp.roundToPx() } } + fadeOut(),
						) {
							TopAppBar(
								title = {},
								navigationIcon = {
									val back = {
										isApplyingFilters = false
										cameraViewModel.discardPicture()
									}
									BackHandler(onBack = back)
									IconButton(onClick = back) {
										Icon(
											Icons.AutoMirrored.Default.ArrowBack,
											contentDescription = "Try again",
										)
									}
								},
								actions = {
									FilledIconToggleButton(
										checked = !isApplyingFilters,
										onCheckedChange = { isApplyingFilters = false }
									) {
										Icon(Icons.Default.Draw, contentDescription = "Draw")
									}
									FilledIconToggleButton(
										checked = isApplyingFilters,
										onCheckedChange = { isApplyingFilters = true }
									) {
										Icon(Icons.Default.AutoFixHigh, contentDescription = "Filters")
									}
									VerticalDivider(Modifier.padding(horizontal = 12.dp, vertical = 16.dp))
									Button(
										onClick = {
											val intent = Intent()
											intent.putExtra("photo", image?.asCompressedByteArray())
											setResult(RESULT_OK, intent)
											finish()
										}
									) {
										Text("Sauvegarder")
									}
								}
							)
						}
					},
					bottomBar = {
						AnimatedVisibility(
							visible = image != null && isApplyingFilters,
							enter = slideInVertically { with(density) { 80.dp.roundToPx() } } + fadeIn(),
							exit = slideOutVertically { with(density) { 80.dp.roundToPx() } } + fadeOut(),
						) {
							BottomAppBar {
								LazyRow(
									modifier = Modifier.padding(horizontal = 16.dp),
									horizontalArrangement = Arrangement.spacedBy(8.dp)
								) {
									items(CameraViewModel.ImageFilter.entries) {
										FilterChip(
											selected = false,
											label = { Text(it.filterName) },
											onClick = { cameraViewModel.filter.value = it },
										)
									}
								}
							}
						}
					}
				) { innerPadding ->
					val navbarPadding = WindowInsets.navigationBars.asPaddingValues()

					val topPadding = innerPadding.calculateTopPadding()
					val bottomPadding = // To make sure animation lands *just right*
						(if (image != null) navbarPadding.calculateBottomPadding() else 0.dp) +
							(if (isApplyingFilters) 80.dp else 0.dp)

					val animateTop by animateDpAsState(topPadding)
					val animateBottom by animateDpAsState(bottomPadding)

					if (permissionGranted) {
						when (image) {
							null -> CameraPreview(viewModel = cameraViewModel)
							else -> Column(
								Modifier
									.padding(top = animateTop, bottom = animateBottom)
									.fillMaxSize()
							) {
								Image(
									cameraViewModel.imageWithFilter.value!!.asImageBitmap(),
									modifier = Modifier.fillMaxSize(),
									contentDescription = null,
									contentScale = ContentScale.Fit,
								)
							}
						}

						if (processing) {
							Column(
								modifier = Modifier
									.fillMaxSize()
									.absoluteOffset(0.dp, 0.dp)
									.background(Color.Black.copy(alpha = 0.5f)),
								horizontalAlignment = Alignment.CenterHorizontally,
								verticalArrangement = Arrangement.Center,
							) {
								CircularProgressIndicator()
							}
						}

						if (image == null && !processing) {
							ShutterButton(viewModel = cameraViewModel)
						}
					}
				}
			}
		}
	}

	@Composable
	private fun RequestCamera() {
		val contract = ActivityResultContracts.RequestPermission()
		val permissionLauncher = rememberLauncherForActivityResult(contract) {
			if (!it) {
				Toast.makeText(
					this,
					"Vous n'avez pas accordé la permission d'utiliser l'appareil photo",
					Toast.LENGTH_LONG
				).show()
				finish()
			} else {
				permissionGranted.value = true
			}
		}

		LaunchedEffect(Unit) {
			if (checkPermissions(this@PhotoActivity, Manifest.permission.CAMERA)) {
				permissionGranted.value = true
			} else {
				permissionLauncher.launch(Manifest.permission.CAMERA)
			}
		}
	}
}
