package m2sdl.lacuillere.data.repository

import android.os.Bundle
import androidx.core.os.BundleCompat
import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.User

// That's ugly, but it does the trick. cba to get real DI up and running
object RepositoryLocator {
	private lateinit var userRepository: UserRepository
	private lateinit var restaurantRepository: RestaurantRepository
	private lateinit var reviewRepository: ReviewRepository
	private lateinit var reservationRepository: ReservationRepository

	fun getUserRepository() = userRepository
	fun getRestaurantRepository() = restaurantRepository
	fun getReviewRepository() = reviewRepository
	fun getReservationRepository() = reservationRepository

	fun init(bundle: Bundle?) {
		val user = bundle?.getParcelableMutableList<User>("user")
		val resto = bundle?.getParcelableMutableList<Restaurant>("resto")
		val review = bundle?.getParcelableMutableList<Review>("review")
		val rsv = bundle?.getParcelableMutableList<Reservation>("rsv")

		userRepository = UserRepository(user)
		restaurantRepository = RestaurantRepository(resto)
		reviewRepository = ReviewRepository(review)
		reservationRepository = ReservationRepository(rsv)
	}

	fun save(bundle: Bundle) {
		bundle.putParcelableArray("user", userRepository.findAll().toTypedArray())
		bundle.putParcelableArray("resto", restaurantRepository.findAll().toTypedArray())
		bundle.putParcelableArray("review", reviewRepository.findAll().toTypedArray())
		bundle.putParcelableArray("rsv", reservationRepository.findAll().toTypedArray())
	}

	private inline fun <reified T> Bundle.getParcelableMutableList(key: String): MutableList<T>? {
		return BundleCompat.getParcelableArrayList(this, key, T::class.java)?.toMutableList()
	}
}
