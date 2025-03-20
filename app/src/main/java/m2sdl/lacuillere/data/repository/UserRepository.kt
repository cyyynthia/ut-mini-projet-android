package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.R
import m2sdl.lacuillere.data.User
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.util.UUID

class UserRepository(initialData: MutableList<User>?) :
	AbstractRepository<User>(initialData ?: mutableListOf(*BASE_DATA)) {

	fun findMyself() = findById(BOB.id) ?: throw RuntimeException()

	companion object {
		private val BOB = User(
			id = UUID.fromString("7bd006a8-2cc6-46d6-ad8c-d263e8192fe5"),
			name = "Bob le Bricoleur",
			avatar = BitmapOrDrawableRef(R.drawable.bob_le_brico_avatar)
		)

		private val PAYOU = User(
			id = UUID.fromString("520f5c7e-71fd-4adb-8c35-5553cf8bd7c6"),
			name = "Payou",
			avatar = BitmapOrDrawableRef(R.drawable.payou_avatar)
		)

		private val KAYOU = User(
			id = UUID.fromString("31c569ea-5e9b-43b2-94a5-e0a942ba337e"),
			name = "Kayou",
			avatar = BitmapOrDrawableRef(R.drawable.kayou_avatar)
		)

		private val RACAYOU = User(
			id = UUID.fromString("7633aa59-e287-4d81-8b46-13927ee7bde9"),
			name = "Racayou",
			avatar = BitmapOrDrawableRef(-1)
		)

		private val RAYENNE_PARIS = User(
			id = UUID.fromString("53b5b066-1bbb-48c9-a03e-990e352d6057"),
			name = "Rayenne de Paris",
			avatar = BitmapOrDrawableRef(R.drawable.rayenne_de_paris_avatar)
		)

		private val RENE = User(
			id = UUID.fromString("9934b352-6e73-42b7-ab5e-b4b360ffbb85"),
			name = "RENÉ MALLEVILLE",
			avatar = BitmapOrDrawableRef(R.drawable.rene_malleville_avatar)
		)

		private val BASE_DATA = arrayOf<User>(
			BOB,
			PAYOU,
			KAYOU,
			RACAYOU,
			RAYENNE_PARIS,
			RENE,
		)
	}
}
