package m2sdl.lacuillere.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {
	private val _userLocation = mutableStateOf<LatLng?>(null)
	val userLocation: State<LatLng?> = _userLocation

	fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {
		if (ContextCompat.checkSelfPermission(
				context,
				android.Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			try {
				fusedLocationClient.lastLocation
					.addOnSuccessListener { location ->
						location?.let {
							val userLatLng = LatLng(it.latitude, it.longitude)
							_userLocation.value = userLatLng
						}
					}
			} catch (e: SecurityException) {
				Toast.makeText(
					context,
					"Permission for location access was revoked: ${e.localizedMessage}",
					Toast.LENGTH_SHORT
				).show()
			}
		} else {
			Toast.makeText(context, "Location permission is not granted.", Toast.LENGTH_SHORT).show()
		}
	}
}
