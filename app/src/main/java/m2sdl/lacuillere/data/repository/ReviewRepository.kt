package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.User

class ReviewRepository(initialData: MutableList<Review>?) :
	AbstractRepository<Review>(initialData ?: mutableListOf(*BASE_DATA)) {

	fun filterByUser(user: User): List<Review> {
		return filterBy { it.userId == user.id }
	}

	fun filterByRestaurant(resto: Restaurant): List<Review> {
		return filterBy { it.restaurantId == resto.id }
	}

	fun filterByUserAndRestaurant(user: User, resto: Restaurant): List<Review> {
		return filterBy { it.userId == user.id && it.restaurantId == resto.id }
	}

	fun hasUserReviewedWithPicturesRestaurant(user: User, resto: Restaurant): Boolean {
		return database.any { it.userId == user.id && it.restaurantId == resto.id && it.photos.isNotEmpty() }
	}

	companion object {
		val BASE_DATA = arrayOf<Review>()
	}
}
