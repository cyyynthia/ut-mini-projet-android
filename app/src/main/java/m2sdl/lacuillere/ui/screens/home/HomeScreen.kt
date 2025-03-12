package m2sdl.lacuillere.ui.screens.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.matchesDestination
import m2sdl.lacuillere.ui.components.SearchBar
import m2sdl.lacuillere.ui.screens.Home

@Composable
fun HomeScreen(
	fusedLocationClient: FusedLocationProviderClient,
	activity: ComponentActivity,
	restaurants: List<Restaurant>,
	onNavigateToRestaurant: (r: Restaurant) -> Unit,
) {
	val navController = rememberNavController()

	Box(modifier = Modifier.fillMaxSize()) {
		NavHost(navController = navController, startDestination = Home.List) {
			composable<Home.Map> { HomeMapScreen(fusedLocationClient, activity, restaurants, onNavigateToRestaurant) }
			composable<Home.List> { HomeListScreen(restaurants, onNavigateToRestaurant) }
		}

		SearchBar()
		HomeScreenNav(navController)
	}
}

@Composable
fun HomeScreenNav(navController: NavController) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.absoluteOffset(0.dp, 0.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Bottom
	) {
		Box(
			modifier = Modifier
				.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
				.clip(RoundedCornerShape(20.dp))
		) {
			NavigationBar(
				modifier = Modifier
					.width(160.dp)
					.height(44.dp)
			) {
				NavigationBarItem(
					selected = navBackStackEntry.matchesDestination(Home.Map),
					icon = { Icon(Icons.Filled.Map, contentDescription = null) },
					onClick = { navController.navigate(Home.Map) }
				)
				NavigationBarItem(
					selected = navBackStackEntry.matchesDestination(Home.List),
					icon = { Icon(Icons.Filled.TableRows, contentDescription = null) },
					onClick = { navController.navigate(Home.List) }
				)
			}
		}
	}
}
