package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.User

class ReservationRepository(initialData: MutableList<Reservation>?) :
	AbstractRepository<Reservation>(initialData ?: mutableListOf(*BASE_DATA)) {

	fun filterByUser(user: User): List<Reservation> {
		return filterBy { it.userId == user.id }
	}

	companion object {
		val BASE_DATA = arrayOf<Reservation>()
	}
}
