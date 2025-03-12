package m2sdl.lacuillere.ui.screens

import kotlinx.serialization.Serializable

@Serializable
object Home {
	@Serializable
	object Map

	@Serializable
	object List
}

@Serializable
data class Resto(val id: String)
