package m2sdl.lacuillere.data

import com.google.android.gms.maps.model.LatLng
import java.time.LocalTime

data class Restaurant(
	val name: String,
	val about: String,
	val address: String,
	val addressShort: String,
	val telephone: String,
	val openingHours: List<Pair<LocalTime, LocalTime>>,
	val menu: String,
	val photos: List<String>,
	val position: LatLng,
) : Entity()
