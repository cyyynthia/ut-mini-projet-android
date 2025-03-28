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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoScreen(
	restaurant: Restaurant,
	onNavigateToBook: () -> Unit,
	onNavigateToSubmitReview: () -> Unit,
	onBack: () -> Unit,
) {
	var selectedTabIndex by remember { mutableStateOf(0) }
	val tabs = listOf("À propos", "Avis")

	Scaffold(
		topBar = {
			MediumTopAppBar(
				navigationIcon = { BackButton(onBack = onBack, contentDescription = "Retour") },
				title = { Text(restaurant.name) },
			)
		}
	) { innerPadding ->
		Column(Modifier.padding(innerPadding)) {
			PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
				tabs.forEachIndexed { index, title ->
					Tab(
						selected = selectedTabIndex == index,
						onClick = { selectedTabIndex = index },
						text = { Text(title) }
					)
				}
			}

			when (selectedTabIndex) {
				0 -> AboutTab(restaurant, onNavigateToBook)
				1 -> ReviewsTab(restaurant, onNavigateToSubmitReview)
			}
		}
	}
}
