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
) : Entity
