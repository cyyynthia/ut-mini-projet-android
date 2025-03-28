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

package m2sdl.lacuillere.ui.components

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.floor

// Big thanks to the Android developer docs lmao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
	selectedDate: Long,
	onDateSelected: (Long) -> Unit,
	onDismiss: () -> Unit,
) {
	val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

	DatePickerDialog(
		onDismissRequest = onDismiss,
		confirmButton = {
			TextButton(
				onClick = {
					datePickerState.selectedDateMillis?.let {
						onDateSelected(it)
					}
					onDismiss()
				}
			) {
				Text("OK")
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text("Annuler")
			}
		}
	) {
		DatePicker(state = datePickerState)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
	selectedDate: Long,
	onDateSelected: (Long) -> Unit,
	onDismiss: () -> Unit,
) {
	var showDial by remember { mutableStateOf(true) }
	val hoursAndMinutes by derivedStateOf { convertMillisToHoursAndMinutes(selectedDate) }

	val timePickerState = rememberTimePickerState(
		initialHour = hoursAndMinutes.first,
		initialMinute = hoursAndMinutes.second,
		is24Hour = true,
	)

	val toggleIcon = if (showDial) {
		Icons.Filled.EditCalendar
	} else {
		Icons.Filled.AccessTime
	}

	Dialog(onDismissRequest = onDismiss) {
		Surface(
			shape = MaterialTheme.shapes.extraLarge,
			tonalElevation = 6.dp,
			modifier =
				Modifier
					.width(IntrinsicSize.Min)
					.height(IntrinsicSize.Min)
					.background(
						shape = MaterialTheme.shapes.extraLarge,
						color = MaterialTheme.colorScheme.surface
					),
		) {
			Column(
				modifier = Modifier.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 20.dp),
					text = "Sélectionnez une heure",
					style = MaterialTheme.typography.labelMedium
				)

				if (showDial) {
					TimePicker(
						state = timePickerState,
					)
				} else {
					TimeInput(
						state = timePickerState,
					)
				}

				Row(
					modifier = Modifier
						.height(40.dp)
						.fillMaxWidth()
				) {
					IconButton(onClick = { showDial = !showDial }) {
						Icon(
							imageVector = toggleIcon,
							contentDescription = "Time picker type toggle",
						)
					}
					Spacer(modifier = Modifier.weight(1f))
					TextButton(onClick = onDismiss) { Text("Annuler") }
					TextButton(
						onClick = {
							onDismiss()
							onDateSelected(
								setHoursAndMinutesToMillis(
									selectedDate,
									timePickerState.hour,
									timePickerState.minute,
								)
							)
						}
					) { Text("OK") }
				}
			}
		}
	}
}

@Composable
fun DatePickerField(
	selectedDate: Long,
	onDateSelected: (Long) -> Unit,
	label: @Composable () -> Unit,
	placeholder: @Composable () -> Unit = { Text("DD/MM/YYYY") },
	trailingIcon: @Composable () -> Unit = { Icon(Icons.Default.DateRange, contentDescription = "Select date") },
	enabled: Boolean = true,
	modifier: Modifier = Modifier,
) {
	FieldWithDialog(
		value = convertMillisToDate(selectedDate),
		label = label,
		placeholder = placeholder,
		trailingIcon = trailingIcon,
		enabled = enabled,
		modifier = modifier,
	) { onDismiss ->
		DatePickerModal(
			selectedDate = selectedDate,
			onDateSelected = { onDateSelected(it) },
			onDismiss = onDismiss
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
	selectedDate: Long,
	onDateSelected: (Long) -> Unit,
	label: @Composable () -> Unit,
	placeholder: @Composable () -> Unit = { Text("HH:MM") },
	trailingIcon: @Composable () -> Unit = { Icon(Icons.Default.AccessTime, contentDescription = "Select hour") },
	enabled: Boolean = true,
	modifier: Modifier = Modifier,
) {
	FieldWithDialog(
		value = convertMillisToHour(selectedDate),
		label = label,
		placeholder = placeholder,
		trailingIcon = trailingIcon,
		enabled = enabled,
		modifier = modifier,
	) { onDismiss ->
		TimePickerModal(
			selectedDate = selectedDate,
			onDateSelected = { onDateSelected(it) },
			onDismiss = onDismiss
		)
	}
}

@Composable
fun DateTimePickerField(
	selectedDate: Long,
	dateLabel: @Composable () -> Unit,
	timeLabel: @Composable () -> Unit,
	onDateSelected: (Long) -> Unit,
	enabled: Boolean = true,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		DatePickerField(
			label = dateLabel,
			selectedDate = selectedDate,
			onDateSelected = onDateSelected,
			modifier = Modifier.weight(3f),
			enabled = enabled,
		)

		TimePickerField(
			label = timeLabel,
			selectedDate = selectedDate,
			onDateSelected = onDateSelected,
			modifier = Modifier.weight(2f),
			enabled = enabled,
		)
	}
}

fun convertMillisToDate(millis: Long): String {
	val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
	return formatter.format(Date(millis))
}


fun convertMillisToHour(millis: Long): String {
	val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
	return formatter.format(Date(millis))
}

fun convertMillisToHoursAndMinutes(millis: Long): Pair<Int, Int> {
	val cal = GregorianCalendar.getInstance()
	cal.timeInMillis = millis

	return Pair(
		cal.get(Calendar.HOUR_OF_DAY),
		cal.get(Calendar.MINUTE),
	)

	// https://gist.github.com/timvisee/fcda9bbdff88d45cc9061606b4b923ca
	// After a thorough evaluation of the situation (2.1s of thinking), I decided that I don't care.
	val millisInDay = 60e3 * 60 * 24
	val millisInHour = 60e3 * 60
	val millisInMinute = 60e3

	val time = (millis % millisInDay).toLong()
	val hours = floor(time / millisInHour).toInt()
	val minutes = floor((time - hours * millisInHour) / millisInMinute).toInt()
	return Pair(hours, minutes)
}

fun setHoursAndMinutesToMillis(millis: Long, hours: Int, minutes: Int): Long {
	val cal = GregorianCalendar.getInstance()
	cal.timeInMillis = millis

	cal.set(Calendar.HOUR_OF_DAY, hours)
	cal.set(Calendar.MINUTE, minutes)

	return cal.timeInMillis
}
