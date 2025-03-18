package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Restaurant

class RestaurantRepository(initialData: MutableList<Restaurant>?) :
	AbstractRepository<Restaurant>(initialData ?: mutableListOf(*BASE_DATA)) {

	companion object {
		val BASE_DATA = arrayOf<Restaurant>()
	}
}
