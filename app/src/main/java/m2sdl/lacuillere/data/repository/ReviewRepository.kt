package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Review

class ReviewRepository : AbstractRepository<Review>() {
	override val database = mutableListOf(*BASE_DATA)

	companion object {
		val BASE_DATA = arrayOf<Review>()
	}
}
