package m2sdl.lacuillere

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute

// Thanks https://slack-chats.kotlinlang.org/t/22714748/when-using-jetpack-compose-navigation-by-google-if-the-route#559bf40c-e8be-4c3f-9a6b-07e1bc5169a5 !
inline fun <reified T : Any> NavBackStackEntry?.matchesDestination(expectedInstance: T): Boolean {
	return this?.let {
		if (!this.destination.hasRoute<T>()) return false
		val route = this.toRoute<T>()
		return route == expectedInstance
	} ?: false
}
