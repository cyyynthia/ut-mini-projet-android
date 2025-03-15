package m2sdl.lacuillere.viewmodel

import android.Manifest
import android.content.Context
import android.location.LocationManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import m2sdl.lacuillere.checkPermissions

class MapViewModel : ViewModel() {
	private val _userLocation = mutableStateOf<LatLng?>(null)
	private val _locationError = mutableStateOf<LocationError?>(null)

	val userLocation: State<LatLng?> = _userLocation
	val locationError: State<LocationError?> = _locationError

	fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		val isLocationNetworkProviderAvailable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
		val isLocationGpsProviderAvailable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

		if (!isLocationGpsProviderAvailable && !isLocationNetworkProviderAvailable) {
			_locationError.value = LocationError.LocationUnavailable
		}

		if (checkPermissions(
				context,
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION
			)
		) {
			try {
				fusedLocationClient.lastLocation
					.addOnSuccessListener { location ->
						location?.let {
							val userLatLng = LatLng(it.latitude, it.longitude)
							_userLocation.value = userLatLng
						}
					}
			} catch (_: SecurityException) {
				_locationError.value = LocationError.PermissionDenied
			}
		} else {
			_locationError.value = LocationError.PermissionDenied
		}
	}

	enum class LocationError {
		LocationUnavailable,
		PermissionDenied,
	}
}
