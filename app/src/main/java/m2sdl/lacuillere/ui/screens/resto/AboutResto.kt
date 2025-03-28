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

package m2sdl.lacuillere.ui.screens.resto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant

@Composable
fun AboutTab(restaurant: Restaurant, onNavigateToBook: () -> Unit) {
	Scaffold(
		floatingActionButton = {
			ExtendedFloatingActionButton(
				onClick = onNavigateToBook,
				icon = { Icon(Icons.Filled.CalendarMonth, null) },
				text = { Text(text = "Réserver") },
			)
		}
	) {
		Column(
			modifier = Modifier
				.verticalScroll(rememberScrollState())
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			restaurant.banner.HackyImage(
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.height(120.dp)
					.clip(RoundedCornerShape(16.dp))
			)

			Text(restaurant.about, style = MaterialTheme.typography.bodyLarge)

			HorizontalDivider()

			Text("Informations", style = MaterialTheme.typography.titleLarge)

			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						Icons.Default.LocationOn,
						contentDescription = null,
						modifier = Modifier.size(MaterialTheme.typography.bodyLarge.fontSize.value.dp),
					)
					Text(restaurant.address)
				}
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						Icons.Default.Phone,
						contentDescription = null,
						modifier = Modifier.size(MaterialTheme.typography.bodyLarge.fontSize.value.dp),
					)
					Text(restaurant.telephone)
				}
				Row(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						Icons.Default.AccessTime,
						contentDescription = null,
						modifier = Modifier.size(MaterialTheme.typography.bodyLarge.fontSize.value.dp),
					)
					Text("${restaurant.openingHours.first().first} - ${restaurant.openingHours.first().second}")
				}
			}

			HorizontalDivider()
			Text("Menu", style = MaterialTheme.typography.titleLarge)
			Text(restaurant.menu)
		}
	}
}
