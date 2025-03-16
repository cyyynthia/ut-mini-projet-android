package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Restaurant

class RestaurantRepository : AbstractRepository<Restaurant>() {
	override val database = mutableListOf(*BASE_DATA)

	companion object {
		val BASE_DATA = arrayOf<Restaurant>()
	}
}
