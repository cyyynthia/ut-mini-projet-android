package m2sdl.lacuillere.ui.screens

import kotlinx.serialization.Serializable
import m2sdl.lacuillere.SUUID

@Serializable
object Home

@Serializable
data class Resto(val id: SUUID)
