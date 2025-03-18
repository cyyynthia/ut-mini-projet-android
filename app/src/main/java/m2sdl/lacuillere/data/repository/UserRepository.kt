package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.data.User

class UserRepository(initialData: MutableList<User>?) :
	AbstractRepository<User>(initialData ?: mutableListOf(*BASE_DATA)) {

	companion object {
		// val PAYOU = User(name = "Payou", avatar = TODO())
		// val KAYOU = User(name = "Kayou", avatar = TODO())
		// val RACAYOU = User(name = "Racayou", avatar = TODO())
		// val RENE = User(name = "RENÉ MALLEVILLE", avatar = TODO())

		val BASE_DATA = arrayOf<User>()
	}
}
