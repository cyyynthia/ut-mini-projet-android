package m2sdl.lacuillere.data

import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.time.LocalTime
import java.util.UUID

@Parcelize
data class Restaurant(
	override val id: UUID = UUID.randomUUID(),
	val name: String,
	val about: String,
	val address: String,
	val addressShort: String,
	val telephone: String,
	val openingHours: List<Pair<LocalTime, LocalTime>>,
	val menu: String,
	val banner: BitmapOrDrawableRef,
	val photos: List<BitmapOrDrawableRef>,
	val position: LatLng,
) : Entity {
	fun isCurrentlyOpen(): Boolean {
		val lt = LocalTime.now()
		return openingHours.any {
			if (it.first > it.second) {
				// Transition over midnight. Think the other way around; check we're NOT inside the first--end range
				!(it.first >= lt && it.second <= lt)
			} else {
				it.first >= lt && it.second <= lt
			}
		}
	}

	fun nextOpeningHours(): LocalTime {
		// Deals with transitions over midnight. KISS
		if (openingHours.size == 1) return openingHours[0].first

		val lt = LocalTime.now()
		return openingHours.firstOrNull { it.first < lt }?.first
			?: openingHours.first().first
	}

	fun nextClosingHours(): LocalTime {
		// Deals with transitions over midnight. KISS
		if (openingHours.size == 1) return openingHours[0].second

		val lt = LocalTime.now()
		return openingHours.firstOrNull { it.first >=  lt }?.second
			?: openingHours.first().second // This would definitely be a suspicious case... cba to deal with it properly.
	}
}
