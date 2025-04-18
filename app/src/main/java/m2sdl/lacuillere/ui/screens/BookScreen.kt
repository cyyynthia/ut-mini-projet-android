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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.hideKeyboardOnOutsideClick
import m2sdl.lacuillere.ui.components.BackButton
import m2sdl.lacuillere.ui.components.DateTimePickerField
import m2sdl.lacuillere.ui.components.LinearLoadingOverlay
import m2sdl.lacuillere.ui.components.RestoCard
import java.util.Date
import kotlin.time.Duration.Companion.seconds

private enum class BookScreenState {
	Filling,
	Submitting,
	Success,
	Closing,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoBookScreen(restaurant: Restaurant, onBack: () -> Unit) {
	val keyboardController = LocalSoftwareKeyboardController.current

	val scope = rememberCoroutineScope()
	var state by rememberSaveable { mutableStateOf(BookScreenState.Filling) }

	var name by rememberSaveable { mutableStateOf("") }
	var phoneNumber by rememberSaveable { mutableStateOf("") }
	var guests by rememberSaveable { mutableStateOf(1) }
	var datetime by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

	// Should be tucked in a view model or smth. eh.
	// We also don't do validation... which shouldn't be too hard to do
	// It's mostly a few derivedStateOf and adding error to fields :shrug:
	fun doSubmit() {
		state = BookScreenState.Submitting

		scope.launch {
			delay(2.seconds)
			val me = RepositoryLocator.getUserRepository().findMyself()
			RepositoryLocator.getReservationRepository().insert(
				Reservation(
					userId = me.id,
					restaurantId = restaurant.id,
					name = name.toString(),
					phone = phoneNumber.toString(),
					peopleCount = guests,
					date = Date(datetime)
				)
			)

			state = BookScreenState.Success
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				navigationIcon = {
					BackButton(onBack = onBack, contentDescription = "Retour")
				},
				title = { Text("Réserver une table") }
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.verticalScroll(rememberScrollState())
				.hideKeyboardOnOutsideClick()
				.padding(horizontal = 16.dp, vertical = 8.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			RestoCard(restaurant)

			OutlinedTextField(
				value = name,
				onValueChange = { name = it },
				label = { Text("Nom") },
				modifier = Modifier.fillMaxWidth(),
				keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
				keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
				maxLines = 1,
			)

			OutlinedTextField(
				value = phoneNumber,
				onValueChange = { phoneNumber = it },
				label = { Text("Numéro de téléphone") },
				modifier = Modifier.fillMaxWidth(),
				keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Phone),
				keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
				maxLines = 1,
			)

			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				Text("Nombre de personnes: ", modifier = Modifier.padding(horizontal = 16.dp))
				Button(onClick = { if (guests > 1) guests-- }) {
					Text("-")
				}
				Text(guests.toString(), modifier = Modifier.padding(horizontal = 16.dp))
				Button(onClick = { guests++ }) {
					Text("+")
				}
			}

			DateTimePickerField(
				selectedDate = datetime,
				onDateSelected = { datetime = it },
				dateLabel = { Text("Date de réservation") },
				timeLabel = { Text("Heure") }
			)

			Spacer(Modifier.weight(1f))

			Button(
				modifier = Modifier.fillMaxWidth(),
				enabled = state == BookScreenState.Filling,
				onClick = { doSubmit() }
			) {
				Text("Réserver")
			}
		}

		LinearLoadingOverlay(
			state == BookScreenState.Submitting,
			Modifier.padding(innerPadding)
		)
	}

	if (state == BookScreenState.Success) {
		val action = {
			state = BookScreenState.Closing
			onBack()
		}

		AlertDialog(
			icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
			title = { Text("Réservation confirmée") },
			text = { Text("Votre réservation a bien été enregistrée. Un email récapitulatif vous a été envoyé.") },
			onDismissRequest = action,
			confirmButton = {
				TextButton(onClick = action) {
					Text("OK")
				}
			},
		)
	}
}
