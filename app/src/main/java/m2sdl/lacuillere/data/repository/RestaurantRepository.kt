package m2sdl.lacuillere.data.repository

import com.google.android.gms.maps.model.LatLng
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.time.LocalTime
import java.util.UUID
import m2sdl.lacuillere.R


class RestaurantRepository(initialData: MutableList<Restaurant>?) :
	AbstractRepository<Restaurant>(initialData ?: mutableListOf(*BASE_DATA)) {

	companion object {
		private val FAKE_MENU: String = """
			Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
			
			- Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
			- Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
			- Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
			- Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
		""".trimIndent()

		private val RU_LE_CANAL = Restaurant(
			id = UUID.fromString("7639afac-6e6e-4e39-98b9-63afa7682a92"),
			name = "Resto U' Le Canal",
			about = "Restaurant universitaire du campus Rangueil de l'Université de Toulouse, accessible PMR et avec parking. Dispose d'une caféteria, avec café, croissants, et chocolatines.",
			address = "585 Cr Rosalind Franklin, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 62 25 62 19",
			openingHours = listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_u_lecanal_front),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_u_lecanal_front)),
			position = LatLng(43.5609537, 1.4718944)
		)

		private val RU_LE_THEOREME = Restaurant(
			id = UUID.fromString("8e569458-3fda-4e4b-991d-53b4516ab43b"),
			name = "Resto U' Le Théorème",
			about = "Restaurant universitaire du campus Rangueil de l'Université de Toulouse, accessible PMR et avec parking. Dispose d'une caféteria, avec café, croissants, et chocolatines.",
			address = "118 Rte de Narbonne, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 62 25 62 09",
			openingHours = listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_theo_front),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_theo_front)),
			position = LatLng(43.5609537, 1.4718944)
		)

		private val RU_MEDECINE = Restaurant(
			id = UUID.fromString("92a98a48-8c08-4745-9be7-8093fa031755"),
			name = "Resto U' Médecine",
			about = "Restaurant universitaire de la Faculté de Médecine de l'Université de Toulouse, accessible PMR et avec parking. Dispose d'une caféteria, avec café, croissants, et chocolatines.",
			address = "133 Rte de Narbonne, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 62 25 62 25",
			openingHours = listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_med_front),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_med_front)),
			position = LatLng(43.5609537, 1.4718944)
		)

		private val CROUS_TRUCK = Restaurant(
			id = UUID.fromString("e4f6ab58-3407-482a-bf80-a4b82b6f70bb"),
			name = "Crous Truck",
			about = "Burgers faits sur place, avec toutes les semaines un gratiné différent, au sein du campus Rangueil de l'Université de Toulouse.",
			address = "325 All. Théodore Despeyrous, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 62 25 62 09",
			openingHours = listOf(Pair(LocalTime.of(11, 45), LocalTime.of(13, 15))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_croustruck),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_croustruck)),
			position = LatLng(43.562276, 1.469147)
		)

		private val MC_DONALDS = Restaurant(
			id = UUID.fromString("8388c263-572e-4f89-882f-f591502eb8a5"),
			name = "McDonald's",
			about = "Venez déguster un Big Mac commes vous êtes. Sauf nu. Venez habillés, quand même.",
			address = "206-210 Rte de Narbonne, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 62 17 08 77",
			openingHours = listOf(Pair(LocalTime.of(11, 45), LocalTime.of(13, 15))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_mcdo_logo),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_mcdo_logo)),
			position = LatLng(43.562276, 1.469147)
		)

		private val SUBWAY = Restaurant(
			id = UUID.fromString("ddc50316-5460-4a2a-b891-14ef63121f73"),
			name = "Subway",
			about = "Mangez frais ! Préparé devant vos yeux, c'est vous le chef.",
			address = "211 Rte de Narbonne, 31400 Toulouse",
			addressShort = "Rangueil, Toulouse",
			telephone = "05 61 52 36 41",
			openingHours = listOf(Pair(LocalTime.of(11, 45), LocalTime.of(13, 15))),
			menu = FAKE_MENU,
			banner = BitmapOrDrawableRef(R.drawable.resto_subway_logo),
			photos = listOf(BitmapOrDrawableRef(R.drawable.resto_subway_logo)),
			position = LatLng(43.562276, 1.469147)
		)

		private val BASE_DATA = arrayOf<Restaurant>(
			RU_LE_CANAL,
			RU_LE_THEOREME,
			RU_MEDECINE,
			CROUS_TRUCK,
			MC_DONALDS,
			SUBWAY,
		)
	}
}
