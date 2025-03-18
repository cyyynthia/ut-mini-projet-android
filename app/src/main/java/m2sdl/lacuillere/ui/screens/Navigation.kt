package m2sdl.lacuillere.ui.screens

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
object Home

@Serializable
data class Resto(private val id: String) {
	constructor(id: UUID) : this(id.toString())

	val uuid: UUID by lazy { UUID.fromString(id) }
}

@Serializable
object ReservationHistory

@Serializable
object ReviewHistory
