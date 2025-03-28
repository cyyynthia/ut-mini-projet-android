/*!
 * Copyright (c) Anton Haehn, Cynthia Rey, All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package m2sdl.lacuillere.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.TableRows
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.components.NavDrawer
import m2sdl.lacuillere.ui.components.SearchBar
import m2sdl.lacuillere.viewmodel.MapViewModel

@Composable
fun HomeScreen(
	model: MapViewModel,
	restaurants: List<Restaurant>,
	onNavigateReservationHistory: () -> Unit,
	onNavigateReviewHistory: () -> Unit,
	onNavigateToRestaurant: (Restaurant) -> Unit,
	onNavigateToReviewsOf: (Restaurant) -> Unit,
) {
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val scope = rememberCoroutineScope()

	var isViewingList by rememberSaveable { mutableStateOf(false) }
	val density = LocalDensity.current

	Scaffold {
		NavDrawer(
			state = drawerState,
			onNavigateReservationHistory = onNavigateReservationHistory,
			onNavigateReviewHistory = onNavigateReviewHistory,
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				// Ugly shenanigans to deal with navigation controller pitfalls & GoogleMap composable!
				// To avoid re-rendering the map when switching back and forth, the map is always rendered.
				// When viewing the lists view, the map is simply hidden behind the list view.
				// See also: https://stackoverflow.com/a/73808783

				HomeMapScreen(
					isDrawerOrListOpen = drawerState.isOpen || isViewingList,
					model = model,
					restaurants = restaurants,
					onNavigateToRestaurant = onNavigateToRestaurant,
					onNavigateToReviewsOf = onNavigateToReviewsOf,
					onMapUnavailable = { isViewingList = true }
				)

				AnimatedVisibility(
					visible = isViewingList,
					enter = slideInVertically { with(density) { 40.dp.roundToPx() } } + fadeIn(),
					exit = slideOutVertically { with(density) { 40.dp.roundToPx() } } + fadeOut(),
				) {
					HomeListScreen(restaurants, onNavigateToRestaurant)
				}

				SearchBar(
					onOpenNav = {
						scope.launch {
							drawerState.apply {
								if (isClosed) open() else close()
							}
						}
					}
				)

				HomeScreenNav(isViewingList, onChange = { isViewingList = it })
			}
		}
	}
}

@Composable
fun HomeScreenNav(isViewingList: Boolean, onChange: (Boolean) -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Bottom,
	) {
		Surface(
			shape = RoundedCornerShape(22.dp),
			tonalElevation = 4.dp,
			shadowElevation = 8.dp,
			modifier = Modifier
				.width(160.dp)
				.height(44.dp)
				.consumeWindowInsets(WindowInsets.navigationBars)
		) {
			NavigationBar(Modifier.fillMaxSize()) {
				NavigationBarItem(
					selected = !isViewingList,
					icon = { Icon(Icons.Filled.Map, contentDescription = null) },
					onClick = { onChange(false) }
				)
				NavigationBarItem(
					selected = isViewingList,
					icon = { Icon(Icons.Filled.TableRows, contentDescription = null) },
					onClick = { onChange(true) }
				)
			}
		}

		Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
	}
}
