package m2sdl.lacuillere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.screens.Home
import m2sdl.lacuillere.ui.screens.home.HomeScreen
import m2sdl.lacuillere.ui.theme.LaCuillereTheme
import java.time.LocalTime

class MainActivity : ComponentActivity() {
	private lateinit var fusedLocationClient: FusedLocationProviderClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
		)

		setContent {
			val navController = rememberNavController()

			LaCuillereTheme {
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					NavHost(navController = navController, startDestination = Home) {
						composable<Home> {
							HomeScreen(
								fusedLocationClient,
								this@MainActivity,
								restaurants,
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	LaCuillereTheme {
		Greeting("Android ajhhae")
	}
}
