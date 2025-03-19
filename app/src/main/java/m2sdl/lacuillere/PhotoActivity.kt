package m2sdl.lacuillere

import android.Manifest
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import m2sdl.lacuillere.resources.Sticker
import m2sdl.lacuillere.ui.components.BackButton
import m2sdl.lacuillere.ui.components.CameraPreview
import m2sdl.lacuillere.ui.components.DrawCanvas
import m2sdl.lacuillere.ui.components.DrawingPropertiesMenu
import m2sdl.lacuillere.ui.components.ShutterButton
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import m2sdl.lacuillere.viewmodel.CameraViewModel
import m2sdl.lacuillere.viewmodel.CanvasViewModel

class PhotoActivity : ComponentActivity(), SensorEventListener {
	companion object {
		init {
			System.loadLibrary("NativeImageProcessor")
		}
	}

	private lateinit var sensorManager: SensorManager
	private var lightSensor: Sensor? = null

	private val permissionGranted = mutableStateOf(false)
	private var ambientLight = -1.0f

	@OptIn(ExperimentalMaterial3Api::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		val insetsController = WindowInsetsControllerCompat(window, window.decorView)
		insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

		sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

		setContent {
			val density = LocalDensity.current
			val permissionGranted by permissionGranted

			val cameraViewModel: CameraViewModel by viewModels()
			val canvasViewModel: CanvasViewModel by viewModels()
			val activityTerminated by cameraViewModel.activityTerminated
			val processing by cameraViewModel.processing
			val image by cameraViewModel.image

			var editState by rememberSaveable { mutableStateOf(EditState.Effects) }
			val ambientLight by rememberUpdatedState(ambientLight)

			var selectingSticker by remember { mutableStateOf(false) }
			var selectedSticker by canvasViewModel.selectedSticker

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
									BackButton(
										contentDescription = "Try again",
										onBack = {
											editState = EditState.Effects
											cameraViewModel.discardPicture()
											canvasViewModel.clear()
										}
									)
								},
								actions = {
									FilledIconToggleButton(
										checked = editState == EditState.Effects,
										onCheckedChange = {
											canvasViewModel.setDrawMode(CanvasViewModel.DrawMode.Idle)
											editState = EditState.Effects
										}
									) {
										Icon(Icons.Default.AutoFixHigh, contentDescription = "Filters")
									}
									FilledIconToggleButton(
										checked = editState == EditState.Stickers,
										onCheckedChange = {
											canvasViewModel.setDrawMode(CanvasViewModel.DrawMode.Sticker)
											editState = EditState.Stickers
										}
									) {
										Icon(Icons.Default.EmojiEmotions, contentDescription = "Add stickers")
									}
									FilledIconToggleButton(
										checked = editState == EditState.Draw,
										onCheckedChange = {
											canvasViewModel.setDrawMode(CanvasViewModel.DrawMode.Draw)
											editState = EditState.Draw
										}
									) {
										Icon(Icons.Default.Draw, contentDescription = "Draw")
									}
									FilledIconToggleButton(
										checked = false,
										onCheckedChange = { notImplementedToast() }
									) {
										Icon(Icons.Default.CropRotate, contentDescription = "Crop and rotate")
									}
									VerticalDivider(Modifier.padding(horizontal = 12.dp, vertical = 16.dp))
									IconButton(
										onClick = {
											val filteredBitmap = cameraViewModel.imageWithFilter.value!!
											val filteredImage = filteredBitmap.asImageBitmap()
											val finalImage = canvasViewModel.draw(this@PhotoActivity, filteredImage)
											val finalBitmap = finalImage.asAndroidBitmap()

											val intent = Intent()
											intent.putExtra("photo", finalBitmap.asCompressedByteArray())
											setResult(RESULT_OK, intent)

											image?.recycle()
											filteredBitmap.recycle()
											finalBitmap.recycle()
											finish()
										}
									) {
										Icon(Icons.Default.Save, contentDescription = "Save")
									}
								}
							)
						}
					},
					bottomBar = {
						AnimatedVisibility(
							visible = image != null,
							enter = slideInVertically { with(density) { 80.dp.roundToPx() } } + fadeIn(),
							exit = slideOutVertically { with(density) { 80.dp.roundToPx() } } + fadeOut(),
						) {
							BottomAppBar {
								when (editState) {
									EditState.Effects ->
										LazyRow(
											modifier = Modifier.padding(horizontal = 16.dp),
											horizontalArrangement = Arrangement.spacedBy(8.dp),
											verticalAlignment = Alignment.CenterVertically,
										) {
											items(CameraViewModel.ImageFilter.entries) {
												val enabled = it.available?.invoke(cameraViewModel) != false
												FilterChip(
													label = { Text(it.filterName) },
													enabled = enabled,
													selected = cameraViewModel.filter.value == it,
													onClick = {
														if (!enabled) return@FilterChip toast("Ce filtre n'est pas disponible.")
														cameraViewModel.filter.value = it
													},
												)
											}
										}

									EditState.Stickers -> {
										Row(
											modifier = Modifier.padding(horizontal = 16.dp),
											horizontalArrangement = Arrangement.spacedBy(8.dp),
											verticalAlignment = Alignment.CenterVertically,
										) {
											Image(
												painterResource(selectedSticker.resource),
												contentDescription = null,
												modifier = Modifier.size(48.dp)
											)
											Button(onClick = { selectingSticker = true }) {
												Text("Changer de sticker")
											}
										}

										if (selectingSticker) {
											ModalBottomSheet(onDismissRequest = { selectingSticker = false }) {
												Column(
													modifier = Modifier.padding(horizontal = 32.dp),
													verticalArrangement = Arrangement.spacedBy(16.dp)
												) {
													Text(
														"Stickers",
														style = MaterialTheme.typography.titleLarge,
														modifier = Modifier
															.basicMarquee()
															.padding(bottom = 16.dp)
													)

													LazyVerticalGrid(
														verticalArrangement = Arrangement.spacedBy(8.dp),
														horizontalArrangement = Arrangement.spacedBy(8.dp),
														columns = GridCells.FixedSize(size = 80.dp)
													) {
														items(Sticker.SORTED) {
															Button(
																colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
																contentPadding = PaddingValues(0.dp),
																shape = RoundedCornerShape(4.dp),
																onClick = {
																	selectedSticker = it
																	selectingSticker = false
																}
															) {
																Image(
																	painterResource(it.resource),
																	contentDescription = null,
																	modifier = Modifier.size(80.dp)
																)
															}
														}
													}
												}
											}
										}
									}

									EditState.Draw ->
										DrawingPropertiesMenu(
											model = canvasViewModel,
											modifier = Modifier
												.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
												.fillMaxWidth()
												.padding(4.dp),
										)
								}
							}
						}
					}
				) {
					if (permissionGranted) {
						when (image) {
							null -> CameraPreview(viewModel = cameraViewModel)
							else -> Column(
								verticalArrangement = Arrangement.Center,
								modifier = Modifier.fillMaxSize()
							) {
								DrawCanvas(
									model = canvasViewModel,
									bitmap = cameraViewModel.imageWithFilter.value!!.asImageBitmap(),
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
							ShutterButton(
								viewModel = cameraViewModel,
								ambientLight = ambientLight,
							)
						}
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
	}

	override fun onPause() {
		super.onPause()
		sensorManager.unregisterListener(this)
	}

	override fun onSensorChanged(event: SensorEvent) {
		when (event.sensor.type) {
			Sensor.TYPE_LIGHT -> {
				ambientLight = event.values[0]

				// Imagine using `Log`, haha >:)
				println("Light sensor $ambientLight")
			}
		}
	}

	override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit

	@Composable
	private fun RequestCamera() {
		val contract = ActivityResultContracts.RequestPermission()
		val permissionLauncher = rememberLauncherForActivityResult(contract) {
			if (!it) {
				toast("Vous n'avez pas accordé la permission d'utiliser l'appareil photo")
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

	enum class EditState {
		Effects, Stickers, Draw
	}
}
