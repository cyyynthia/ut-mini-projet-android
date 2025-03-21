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
data class Book(private val id: String) {
	constructor(id: UUID) : this(id.toString())

	val uuid: UUID by lazy { UUID.fromString(id) }
}

@Serializable
data class SubmitReview(private val id: String) {
	constructor(id: UUID) : this(id.toString())

	val uuid: UUID by lazy { UUID.fromString(id) }
}

@Serializable
object ReservationHistory

@Serializable
data class ReviewHistory(private val id: String?) {
	constructor(id: UUID? = null) : this(id?.toString())

	val uuid: UUID? by lazy { id?.let { UUID.fromString(it) } }
}
