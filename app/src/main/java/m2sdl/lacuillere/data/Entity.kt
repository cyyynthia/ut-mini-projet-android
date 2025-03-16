package m2sdl.lacuillere.data

import java.util.UUID

abstract class Entity {
	val id: UUID = UUID.randomUUID()
}
