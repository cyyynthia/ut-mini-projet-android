package m2sdl.lacuillere.ui.composables

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import m2sdl.lacuillere.viewmodel.MapViewModel

@Composable
fun RequestLocation(model: MapViewModel) {
	val context = LocalContext.current
	val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

	val contract = ActivityResultContracts.RequestPermission()
	val permissionLauncher = rememberLauncherForActivityResult(contract) { isGranted ->
		if (isGranted) {
			model.fetchUserLocation(context, fusedLocationClient)
		} else {
			Toast.makeText(context, "Location permission was denied by the user.", Toast.LENGTH_SHORT).show()
		}
	}

	LaunchedEffect(Unit) {
		when (PackageManager.PERMISSION_GRANTED) {
			ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
				model.fetchUserLocation(context, fusedLocationClient)
			}

			else -> {
				permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
			}
		}
	}
}
