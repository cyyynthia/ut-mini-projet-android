package m2sdl.lacuillere.data

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime
import java.util.Date

data class Restaurant(
	val id: String,
	val name: String,
	val about: String,
	val address: String,
	val addressShort: String,
	val telephone: String,
	// List<[debut, fin]>
	val openingHours: List<Pair<LocalTime, LocalTime>>,
	val menu: String,
	val photos: List<String>,
	val position: LatLng,
)

data class _Review(
	val id: String,
	val userId: String,
	val restaurantId: String,
	val note: Int,
	val text: String,
	val photos: List<String>,
)

data class _Reservation(
	val id: String,
	val userId: String,
	val restaurantId: String,
	val peopleCount: Int,
	val date: Date,
)
