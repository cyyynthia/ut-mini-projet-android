package m2sdl.lacuillere.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaActionSound
import android.media.MediaActionSound.SHUTTER_CLICK
import android.util.Log
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.zomato.photofilters.SampleFilters
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter
import m2sdl.lacuillere.addListener
import m2sdl.lacuillere.toast
import kotlin.math.absoluteValue
import kotlin.math.exp
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class CameraViewModel : ViewModel() {
	private val _activityTerminated = mutableStateOf(false)
	private val _surfaceRequest = mutableStateOf<SurfaceRequest?>(null)
	private val _processing = mutableStateOf<Boolean>(false)
	private val _image = mutableStateOf<Bitmap?>(null)
	private val _cameraAspectRatio = mutableStateOf<Float>(4f / 3f)

	val filter = mutableStateOf(ImageFilter.None)
	val activityTerminated: State<Boolean> = _activityTerminated
	val surfaceRequest: State<SurfaceRequest?> = _surfaceRequest
	val processing: State<Boolean> = _processing
	val image: State<Bitmap?> = _image
	val cameraAspectRatio: State<Float> = _cameraAspectRatio

	private var ambientLightAtCapture: Float = -1.0f

	private var clone: Bitmap? = null
	val imageWithFilter = derivedStateOf {
		clone?.recycle()

		val image = image.value ?: return@derivedStateOf null
		val subfilter = filter.value.builder?.invoke(this) ?: return@derivedStateOf image

		clone = image.copy(image.config!!, true)
		return@derivedStateOf subfilter.processFilter(clone)
	}

	private var camera: Camera? = null

	private val resSelector = ResolutionSelector.Builder()
		.setResolutionStrategy(ResolutionStrategy(Size(1920, 1440), ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER))
		.build()
	private val imageCapture = ImageCapture.Builder()
		.setResolutionSelector(resSelector)
		.setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
		.build()
	private val cameraPreview = Preview.Builder()
		.setResolutionSelector(resSelector)
		.setPreviewStabilizationEnabled(true)
		.build()
		.apply { setSurfaceProvider { _surfaceRequest.value = it } }

	fun startCamera(ctx: Context, lifecycleOwner: LifecycleOwner) {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
		cameraProviderFuture.addListener(ContextCompat.getMainExecutor(ctx)) {
			val cameraProvider = cameraProviderFuture.get()
			val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

			try {
				cameraProvider.unbindAll()
				this.camera = cameraProvider.bindToLifecycle(
					lifecycleOwner,
					cameraSelector,
					cameraPreview,
					imageCapture,
				)

				cameraPreview.resolutionInfo?.let {
					_cameraAspectRatio.value =
						if (it.rotationDegrees % 180 != 0)
							it.resolution.height.toFloat() / it.resolution.width.toFloat()
						else
							it.resolution.width.toFloat() / it.resolution.height.toFloat()
				}
			} catch (ex: Exception) {
				_activityTerminated.value = true
				Log.e("LaCuillèrePhoto", "Use case binding failed", ex)
				ctx.toast("Une erreur est survenue lors du démarrage de l'appareil photo")
			}
		}
	}

	fun takePicture(ctx: Context, ambientLight: Float? = null) {
		val camera = this.camera ?: return

		ambientLightAtCapture = ambientLight ?: -1.0f
		imageCapture.takePicture(
			ContextCompat.getMainExecutor(ctx),
			object : ImageCapture.OnImageCapturedCallback() {
				override fun onCaptureStarted() {
					MediaActionSound().play(SHUTTER_CLICK)
					_processing.value = true
				}

				override fun onCaptureSuccess(image: ImageProxy) {
					_processing.value = false
					val captured = image.toBitmap()

					val displayRotation = ContextCompat.getDisplayOrDefault(ctx).rotation * 90
					val constantRotation = image.imageInfo.rotationDegrees - camera.cameraInfo.sensorRotationDegrees
					val rotationDegrees = camera.cameraInfo.sensorRotationDegrees - displayRotation + constantRotation

					val maxHeight = 1920
					val maxWidth = 1440
					val scale = min(
						(maxHeight.toFloat() / captured.getWidth()).toDouble(),
						(maxWidth.toFloat() / captured.getHeight()).toDouble()
					)

					val matrix = Matrix()
					matrix.postRotate(rotationDegrees.toFloat())
					matrix.postScale(scale.toFloat(), scale.toFloat())

					val bitmap: Bitmap = Bitmap.createBitmap(captured, 0, 0, image.width, image.height, matrix, true)
					captured.recycle()

					_image.value = bitmap
				}

				override fun onError(exception: ImageCaptureException) {
					_processing.value = false
					_activityTerminated.value = true
					ctx.toast(exception.localizedMessage ?: "Une erreur est survenue lors de la prise de la photo.")
				}
			}
		)
	}

	fun discardPicture() {
		_image.value = null
		_surfaceRequest.value = null
		filter.value = ImageFilter.None
	}

	enum class ImageFilter(
		val filterName: String,
		val available: (CameraViewModel.() -> Boolean)? = null,
		val builder: (CameraViewModel.() -> Filter)? = null,
	) {
		None("Aucun"),
		BlackAndWhite(
			"Noir et blanc",
			builder = { Filter().apply { addSubFilter(SaturationSubFilter(0f)) } },
		),
		Awestruck(
			"Émerveillé",
			builder = { SampleFilters.getAweStruckVibeFilter() },
		),
		StarLit(
			"Étoilé",
			builder = { SampleFilters.getStarLitFilter() },
		),
		DynamicBrighten(
			"Luminescence dynamique",
			available = { ambientLightAtCapture >= 0 },
			builder = {
				val brightness = transformAmbientLightToBrightness(ambientLightAtCapture).coerceIn(-1f, 1f)
				Filter().apply { addSubFilter(BrightnessSubFilter((brightness * 100).toInt())) }
			},
		);

		companion object {
			private fun f(x: Float) = 1 - exp(-(x - 35).pow(2) / 200)
			private fun g(x: Float) = x / 35 - 1
			private fun h(x: Float) = h(x, g(x))
			private fun h(x: Float, gx: Float) = -(gx / (1 + gx.absoluteValue)) * f(x) * 2
			private fun i(x: Float) = x / (1 + (sqrt(x.absoluteValue - x) / 2))
			private fun j(x: Float) = i(h(x))

			private fun transformAmbientLightToBrightness(light: Float) = j(light)
		}
	}
}
