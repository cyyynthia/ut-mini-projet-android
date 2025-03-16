package m2sdl.lacuillere.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.util.Size
import android.widget.Toast
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
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter
import m2sdl.lacuillere.addListener

class CameraViewModel : ViewModel() {
	private val _activityTerminated = mutableStateOf(false)
	private val _surfaceRequest = mutableStateOf<SurfaceRequest?>(null)
	private val _processing = mutableStateOf<Boolean>(false)
	private val _image = mutableStateOf<Bitmap?>(null)

	val filter = mutableStateOf(ImageFilter.None)
	val activityTerminated: State<Boolean> = _activityTerminated
	val surfaceRequest: State<SurfaceRequest?> = _surfaceRequest
	val processing: State<Boolean> = _processing
	val image: State<Bitmap?> = _image

	private var clone: Bitmap? = null
	val imageWithFilter = derivedStateOf {
		clone?.recycle()

		val image = image.value ?: return@derivedStateOf null
		val subfilter = filter.value.subfilter ?: return@derivedStateOf image

		clone = image.copy(image.config!!, true)
		return@derivedStateOf subfilter.processFilter(clone)
	}

	private var camera: Camera? = null
	private var cameraProvider: ProcessCameraProvider? = null

	private val resSelector = ResolutionSelector.Builder()
		.setResolutionStrategy(ResolutionStrategy(Size(1920, 1440), ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER))
		.build()
	private val imageCapture = ImageCapture.Builder()
		.setResolutionSelector(resSelector)
		.build()
	private val cameraPreview = Preview.Builder()
		.setResolutionSelector(resSelector)
		.build()
		.apply { setSurfaceProvider { _surfaceRequest.value = it } }

	fun startCamera(ctx: Context, lifecycleOwner: LifecycleOwner) {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
		cameraProviderFuture.addListener(ContextCompat.getMainExecutor(ctx)) {
			val cameraProvider = cameraProviderFuture.get()
			val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

			try {
				cameraProvider.unbindAll()
				this.camera =
					cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, cameraPreview, imageCapture)
				this.cameraProvider = cameraProvider
			} catch (ex: Exception) {
				_activityTerminated.value = true
				Log.e("LaCuillèrePhoto", "Use case binding failed", ex)
				Toast.makeText(ctx, "Une erreur est survenue lors du démarrage de l'appareil photo", Toast.LENGTH_LONG)
					.show()
			}
		}
	}

	fun takePicture(ctx: Context) {
		val cameraProvider = this.cameraProvider ?: return
		val camera = this.camera ?: return

		cameraProvider.unbind(cameraPreview)

		_processing.value = true
		imageCapture.takePicture(
			ContextCompat.getMainExecutor(ctx),
			object : ImageCapture.OnImageCapturedCallback() {
				override fun onCaptureSuccess(image: ImageProxy) {
					_processing.value = false

					val constantRotation = image.imageInfo.rotationDegrees - camera.cameraInfo.sensorRotationDegrees
					val rotationDegrees =
						camera.cameraInfo.sensorRotationDegrees - ContextCompat.getDisplayOrDefault(ctx).rotation * 90 + constantRotation

					val matrix = Matrix()
					matrix.postRotate(rotationDegrees.toFloat())

					val captured = image.toBitmap()
					val bitmap: Bitmap = Bitmap.createBitmap(captured, 0, 0, image.width, image.height, matrix, true)
					captured.recycle()

					_image.value = bitmap
				}

				override fun onError(exception: ImageCaptureException) {
					_processing.value = false
					_activityTerminated.value = true
					Toast.makeText(
						ctx,
						exception.localizedMessage ?: "Une erreur est survenue lors de la prise de la photo.",
						Toast.LENGTH_LONG
					).show()
				}
			}
		)
	}

	fun discardPicture() {
		_image.value = null
		_surfaceRequest.value = null
	}

	enum class ImageFilter(val filterName: String, val subfilter: Filter?) {
		None("Aucun", null),
		BlackAndWhite("Noir et blanc", Filter().apply { addSubFilter(SaturationSubFilter(0f)) }),
		Awestruck("Émerveillé", SampleFilters.getAweStruckVibeFilter()),
		StarLit("Étoilé", SampleFilters.getStarLitFilter()),
	}
}
