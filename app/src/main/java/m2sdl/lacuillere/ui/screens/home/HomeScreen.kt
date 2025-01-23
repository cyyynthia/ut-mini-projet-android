package m2sdl.lacuillere.ui.screens.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.components.SearchBar
import m2sdl.lacuillere.ui.screens.Home

@Composable
fun HomeScreen(
	fusedLocationClient: FusedLocationProviderClient,
	activity: ComponentActivity,
	restaurants: List<Restaurant>
) {
	val navController = rememberNavController()

	Box(modifier = Modifier.fillMaxSize()) {
		NavHost(navController = navController, startDestination = Home.List) {
			composable<Home.Map> { HomeMapScreen(fusedLocationClient, activity, restaurants) }
			composable<Home.List> { HomeListScreen(restaurants) }
		}
		SearchBar()
		HomeScreenNav(
			onNavigateToMap = { navController.navigate(Home.Map) },
			onNavigateToList = { navController.navigate(Home.List) },
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenNav(
	onNavigateToMap: () -> Unit,
	onNavigateToList: () -> Unit,
) {
	var selectedTab by remember { mutableStateOf(0) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.absoluteOffset(0.dp, 0.dp)
			.padding(16.dp),
		verticalArrangement = Arrangement.Bottom
	) {
		NavigationBar {
			NavigationBarItem(
				icon = { Text("Carte") },
				label = { Text("a") },
				selected = selectedTab == 0,
				onClick = { selectedTab = 0; onNavigateToMap() },
			)
			NavigationBarItem(
				icon = { Text("Liste") },
				label = { Text("a") },
				selected = selectedTab == 1,
				onClick = { selectedTab = 1; onNavigateToList() },
			)
		}
	}
}
