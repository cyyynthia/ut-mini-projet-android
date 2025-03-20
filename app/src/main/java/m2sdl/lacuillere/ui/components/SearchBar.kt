package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.notImplementedToast
import androidx.compose.material3.SearchBar as MaterialSearchBar

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun SearchBar(onOpenNav: () -> Unit) {
	var expanded by rememberSaveable { mutableStateOf(false) }

	if (expanded) {
		LocalContext.current.notImplementedToast()
		LocalSoftwareKeyboardController.current?.hide()
		LocalFocusManager.current.clearFocus(true)
		expanded = false
	}

	MaterialSearchBar(
		inputField = {
			InputField(
				query = "",
				onQueryChange = {},
				onSearch = {},

				expanded = expanded,
				onExpandedChange = { expanded = it },
				placeholder = {
					Text("Rechercher un restaurant...")
				},
				leadingIcon = {
					IconButton(onClick = onOpenNav) {
						Icon(
							imageVector = Icons.Default.Menu,
							contentDescription = null,
						)
					}
				},
				trailingIcon = {
					Icon(
						imageVector = Icons.Default.Search,
						contentDescription = null,
					)
				},
			)
		},
		expanded = false,
		onExpandedChange = {},
		shadowElevation = 8.dp,
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 16.dp, top = 8.dp, end = 16.dp)
			.background(Color.Transparent),
	) {}
}
