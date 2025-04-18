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

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import m2sdl.lacuillere.R
import m2sdl.lacuillere.applyTo
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.isNull
import m2sdl.lacuillere.toast
import m2sdl.lacuillere.ui.composables.RequestLocation
import m2sdl.lacuillere.viewmodel.MapViewModel

@Composable
fun LightSystemBar(isDrawerOrListOpen: Boolean) {
	val view = LocalView.current
	val window = (view.context as? Activity)?.window ?: return

	DisposableEffect(view, window, isDrawerOrListOpen) {
		val insetsController = WindowCompat.getInsetsController(window, view)
		val originalStatusBars = insetsController.isAppearanceLightStatusBars
		val originalNavigationBars = insetsController.isAppearanceLightNavigationBars

		insetsController.isAppearanceLightStatusBars = !isDrawerOrListOpen
		insetsController.isAppearanceLightNavigationBars = !isDrawerOrListOpen

		onDispose {
			insetsController.isAppearanceLightStatusBars = originalStatusBars
			insetsController.isAppearanceLightNavigationBars = originalNavigationBars
		}
	}
}

@Composable
fun HomeMapScreen(
	model: MapViewModel,
	isDrawerOrListOpen: Boolean,
	restaurants: List<Restaurant>,
	onNavigateToRestaurant: (Restaurant) -> Unit,
	onNavigateToReviewsOf: (Restaurant) -> Unit,
	onMapUnavailable: (MapViewModel.LocationError) -> Unit,
) {
	val userLocation by model.userLocation
	val locationError by model.locationError
	val cameraPositionState = rememberCameraPositionState { userLocation?.applyTo(this) }
	val coroutineScope = rememberCoroutineScope()

	val myself = RepositoryLocator.getUserRepository().findMyself()
	val reviewRepo = RepositoryLocator.getReviewRepository()
	val restosAndReviewInfo by derivedStateOf {
		restaurants.map {
			it to reviewRepo.hasUserReviewedWithPicturesRestaurant(myself, it)
		}
	}

	locationError?.let {
		when (it) {
			MapViewModel.LocationError.LocationUnavailable ->
				LocalContext.current.toast("Aucun service de localisation est activé.")

			MapViewModel.LocationError.PermissionDenied ->
				LocalContext.current.toast("Vous n'avez pas accordé la permission d'accéder à votre position.")
		}

		if (cameraPositionState.isNull()) {
			onMapUnavailable(it)
		}
	}

	if (cameraPositionState.isNull()) {
		userLocation?.applyTo(cameraPositionState)
			?: RequestLocation(model)
	} else {
		LightSystemBar(isDrawerOrListOpen)

		GoogleMap(
			modifier = Modifier.fillMaxSize(),
			cameraPositionState = cameraPositionState,
			properties = MapProperties(
				isBuildingEnabled = true,
				isTrafficEnabled = true,
				mapStyleOptions = MapStyleOptions.loadRawResourceStyle(LocalContext.current, R.raw.map_style)
			)
		) {
			restosAndReviewInfo.forEach { restoAndReview ->
				val restaurant = restoAndReview.first
				val hasReviewed = restoAndReview.second

				Marker(
					icon = BitmapDescriptorFactory.defaultMarker(
						if (hasReviewed) 200f else 0f
					),

					state = MarkerState(
						position = restaurant.position,
					),
					title = restaurant.name,
					snippet =
						if (hasReviewed) "Vous avez des images d'ici!"
						else "Cliquez ici pour voir le restaurant",
					onInfoWindowClick = { onNavigateToRestaurant(restaurant) },
					onInfoWindowLongClick = {
						if (hasReviewed) {
							onNavigateToReviewsOf(restaurant)
						}
					},
					onClick = {
						coroutineScope.launch {
							cameraPositionState.animate(
								update = CameraUpdateFactory.newLatLng(restaurant.position),
								durationMs = 500
							)
						}

						false
					}
				)
			}
		}
	}
}
