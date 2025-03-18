package m2sdl.lacuillere.ui.screens.book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.Restaurant
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestoBookScreen(restaurant: Restaurant) {
	var name by remember { mutableStateOf(TextFieldValue("")) }
	var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
	var guests by remember { mutableStateOf(1) }

	val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

	val currentTime = Calendar.getInstance()

	val timePickerState = rememberTimePickerState(
		initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
		initialMinute = currentTime.get(Calendar.MINUTE),
		is24Hour = true,
	)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Text(
			text = "Réserver au ${restaurant.name}",
			style = MaterialTheme.typography.headlineLarge,
			modifier = Modifier.padding(bottom = 16.dp)
		)
		OutlinedTextField(
			value = name,
			onValueChange = { name = it },
			label = { Text("Nom") },
			modifier = Modifier.fillMaxWidth()
		)
		Spacer(modifier = Modifier.height(8.dp))
		OutlinedTextField(
			value = phoneNumber,
			onValueChange = { phoneNumber = it },
			label = { Text("Numéro de téléphone") },
			modifier = Modifier.fillMaxWidth()
		)
		Spacer(modifier = Modifier.height(8.dp))
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
		Spacer(modifier = Modifier.height(16.dp))
		DatePicker(
			state = datePickerState,
			modifier = Modifier.align(Alignment.CenterHorizontally),
			colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
		)

		TimeInput(
			state = timePickerState,
			modifier = Modifier
				.minimumInteractiveComponentSize()
				.align(Alignment.CenterHorizontally)
				.padding(top = 8.dp, bottom = 16.dp)
		)

		Button(
			onClick = { //Faire page resto reserver
			},
			modifier = Modifier.fillMaxWidth()
		) {
			Text("Réserver")
		}
	}
}

