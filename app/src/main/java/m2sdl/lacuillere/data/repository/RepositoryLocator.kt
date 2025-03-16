package m2sdl.lacuillere.data.repository

// That's ugly, but it does the trick. cba to get real DI up and running
object RepositoryLocator {
	private val userRepository = UserRepository()
	private val restaurantRepository = RestaurantRepository()
	private val reviewRepository = ReviewRepository()
	private val reservationRepository = ReservationRepository()

	fun getUserRepository() = userRepository
	fun getRestaurantRepository() = restaurantRepository
	fun getReviewRepository() = reviewRepository
	fun getReservationRepository() = reservationRepository
}
