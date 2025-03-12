package m2sdl.lacuillere.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import m2sdl.lacuillere.R
import m2sdl.lacuillere.data.Restaurant

const val ZOOM_FACTOR = 15f

@Composable
fun HomeMapScreen(
	fusedLocationClient: FusedLocationProviderClient,
	activity: ComponentActivity,
	restaurants: List<Restaurant>,
	onNavigateToRestaurant: (r: Restaurant) -> Unit,
) {
	val cameraPositionState = currentPositionCameraState(activity, fusedLocationClient)

	GoogleMap(
		modifier = Modifier.fillMaxSize(), properties = MapProperties(
			isBuildingEnabled = true,
			isTrafficEnabled = true,
			mapStyleOptions = MapStyleOptions.loadRawResourceStyle(activity, R.raw.map_style)
		), cameraPositionState = cameraPositionState
	) {
		restaurants.forEach { restaurant ->
			Marker(
				state = MarkerState(
					position = LatLng(
						restaurant.position.latitude, restaurant.position.longitude
					)
				), title = restaurant.name
			)
		}
	}
}

@Composable
fun currentPositionCameraState(
	activity: ComponentActivity,
	fusedLocationClient: FusedLocationProviderClient,
): CameraPositionState {
	val state = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), ZOOM_FACTOR)
	}

	val fetchLocation = {
		fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener {
			it?.let {
				return@addOnSuccessListener state.move(
					CameraUpdateFactory.newCameraPosition(
						CameraPosition.fromLatLngZoom(
							LatLng(it.latitude, it.longitude), ZOOM_FACTOR
						)
					)
				)
			}

			Toast.makeText(activity, "Failed to retrieve location.", Toast.LENGTH_SHORT).show()
		}.addOnFailureListener {
			Toast.makeText(activity, "Failed to retrieve location.", Toast.LENGTH_SHORT).show()
		}
	}

	val requestMultiplePermissions = rememberLauncherForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) {
		val granted = it.any()
		if (!granted) {
			Toast.makeText(
				activity, "You denied access to your location. This will degrade your experience.", Toast.LENGTH_SHORT
			).show()
		} else {
			fetchLocation()
		}
	}

	val fineLocationGranted = ActivityCompat.checkSelfPermission(
		activity, Manifest.permission.ACCESS_FINE_LOCATION
	) == PackageManager.PERMISSION_GRANTED
	val coarseLocationGranted = ActivityCompat.checkSelfPermission(
		activity, Manifest.permission.ACCESS_COARSE_LOCATION
	) == PackageManager.PERMISSION_GRANTED

	if (!fineLocationGranted && !coarseLocationGranted) {
		val coroutineScope = rememberCoroutineScope()
		LaunchedEffect(coroutineScope) {
			requestMultiplePermissions.launch(
				arrayOf(
					Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
				)
			)
		}
	} else {
		fetchLocation()
	}

	return state
}
