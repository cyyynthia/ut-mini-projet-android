package m2sdl.lacuillere.ui.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationServices
import m2sdl.lacuillere.checkPermissions
import m2sdl.lacuillere.viewmodel.MapViewModel

val permissions = arrayOf(
	Manifest.permission.ACCESS_COARSE_LOCATION,
	Manifest.permission.ACCESS_FINE_LOCATION
)

@Composable
fun RequestLocation(model: MapViewModel) {
	val context = LocalContext.current
	val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

	val contract = ActivityResultContracts.RequestMultiplePermissions()
	val permissionLauncher = rememberLauncherForActivityResult(contract) {
		model.fetchUserLocation(context, fusedLocationClient)
	}

	LaunchedEffect(Unit) {
		if (checkPermissions(context, *permissions)) {
			model.fetchUserLocation(context, fusedLocationClient)
		} else {
			permissionLauncher.launch(permissions)
		}
	}
}
