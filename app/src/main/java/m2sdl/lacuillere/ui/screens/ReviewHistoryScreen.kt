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

package m2sdl.lacuillere.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.ui.components.BackButton
import m2sdl.lacuillere.ui.components.EmptyScreen
import m2sdl.lacuillere.ui.components.ReviewListHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewHistoryScreen(targetRestaurant: Restaurant? = null, onBack: () -> Unit) {
	val myself = remember { RepositoryLocator.getUserRepository().findMyself() }
	val reviews = remember {
		if (targetRestaurant != null)
			RepositoryLocator.getReviewRepository().filterByUserAndRestaurant(myself, targetRestaurant)
		else
			RepositoryLocator.getReviewRepository().filterByUser(myself)
	}

	Scaffold(
		topBar = {
			TopAppBar(
				navigationIcon = { BackButton(onBack = onBack, contentDescription = "Retour") },
				title = { Text("Mes avis") }
			)
		}
	) { innerPadding ->
		if (reviews.isEmpty()) {
			EmptyScreen(
				icon = Icons.Filled.Reviews,
				text = "Vous n'avez jamais laissé d'avis",
			)
		} else {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(innerPadding)
					.padding(start = 16.dp, end = 16.dp, top = 8.dp),
			) {
				itemsIndexed(reviews) { index, review ->
					ReviewListHistoryItem(review)

					if (index < reviews.lastIndex)
						HorizontalDivider(Modifier.padding(horizontal = 16.dp))
				}
			}
		}
	}
}
