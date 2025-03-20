package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.notImplementedToast

@Composable
fun NavDrawer(
	state: DrawerState,
	onNavigateReservationHistory: () -> Unit,
	onNavigateReviewHistory: () -> Unit,
	content: @Composable () -> Unit,
) {
	val ctx = LocalContext.current
	val myself = remember { RepositoryLocator.getUserRepository().findMyself() }
	val gesturesEnabled by derivedStateOf { state.isOpen }

	ModalNavigationDrawer(
		drawerState = state,
		gesturesEnabled = gesturesEnabled,
		drawerContent = {
			ModalDrawerSheet {
				Spacer(Modifier.height(12.dp))
				myself.avatar.HackyImage(
					contentDescription = null,
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 8.dp)
						.size(56.dp, 56.dp)
						.clip(RoundedCornerShape(20.dp)),
					contentScale = ContentScale.Crop
				)
				Text(
					myself.name,
					modifier = Modifier.padding(horizontal = 16.dp),
					style = MaterialTheme.typography.titleLarge
				)
				Text(
					"email@domain.example",
					modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
					style = MaterialTheme.typography.titleSmall
				)
				HorizontalDivider()

				Text("Mon activité", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
				NavigationDrawerItem(
					label = { Text("Réservations") },
					selected = false,
					icon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null) },
					onClick = onNavigateReservationHistory,
				)
				NavigationDrawerItem(
					label = { Text("Avis") },
					selected = false,
					icon = { Icon(Icons.Outlined.Reviews, contentDescription = null) },
					onClick = onNavigateReviewHistory,
				)

				HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
				NavigationDrawerItem(
					label = { Text("Paramètres") },
					selected = false,
					icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
					onClick = { ctx.notImplementedToast() }
				)
				NavigationDrawerItem(
					label = { Text("Aide et commentaires") },
					selected = false,
					icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
					onClick = { ctx.notImplementedToast() },
				)
				Spacer(Modifier.height(12.dp))
			}
		},
	) {
		content()
	}
}
