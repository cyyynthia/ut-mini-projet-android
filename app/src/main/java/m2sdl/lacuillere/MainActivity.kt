package m2sdl.lacuillere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.android.gms.maps.model.LatLng
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.screens.Home
import m2sdl.lacuillere.ui.screens.Resto
import m2sdl.lacuillere.ui.screens.home.HomeScreen
import m2sdl.lacuillere.ui.screens.resto.RestoScreen
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import m2sdl.lacuillere.viewmodel.MapViewModel
import java.time.LocalTime

class MainActivity : ComponentActivity() {
	companion object {
		init {
			System.loadLibrary("NativeImageProcessor")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		val restaurants = listOf(
			Restaurant(
				"abcdef",
				"Resto U' Le Canal",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5609537, 1.4718944)
			),
			Restaurant(
				"ghijkl",
				"Resto u' Le Theorème",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5622513, 1.463182)
			),
			Restaurant(
				"abcde",
				"Resto U' Le Canal",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5609537, 1.4718944)
			),
			Restaurant(
				"ghijl",
				"Resto u' Le Theorème",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5622513, 1.463182)
			),
			Restaurant(
				"abcef",
				"Resto U' Le Canal",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5609537, 1.4718944)
			),
			Restaurant(
				"ghjkl",
				"Resto u' Le Theorème",
				"About",
				"[...] Université de Toulouse",
				"Rangueil, Toulouse",
				"0500505005",
				listOf(Pair(LocalTime.of(11, 30), LocalTime.of(13, 30))),
				"Menu Burger",
				listOf("photo1", "photo2"),
				LatLng(43.5622513, 1.463182)
			),
		)

		setContent {
			val navController = rememberNavController()
			val mapViewModel: MapViewModel by viewModels()

			LaCuillereTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					NavHost(navController = navController, startDestination = Home) {
						composable<Home> {
							HomeScreen(
								model = mapViewModel,
								restaurants = restaurants,
								onNavigateToRestaurant = { navController.navigate(Resto(it.id)) }
							)
						}

						composable<Resto> { backStackEntry ->
							val resto: Resto = backStackEntry.toRoute()
							val restaurant = restaurants.find { it.id == resto.id }
							restaurant?.let { RestoScreen(it) } ?: navController.navigate(Home)
						}
					}
				}
			}
		}
	}
}
