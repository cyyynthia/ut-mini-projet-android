package m2sdl.lacuillere.data

import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.util.UUID

@Parcelize
data class User(
	override val id: UUID = UUID.randomUUID(),
	val name: String,
	val avatar: BitmapOrDrawableRef,
) : Entity
