package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Review

class ReviewRepository(initialData: MutableList<Review>?) :
	AbstractRepository<Review>(initialData ?: mutableListOf(*BASE_DATA)) {

	companion object {
		val BASE_DATA = arrayOf<Review>()
	}
}
