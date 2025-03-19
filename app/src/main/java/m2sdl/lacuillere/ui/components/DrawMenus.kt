/*!
 * Original work's copyright notice
 * ================================
 * Copyright (c) 2023 Smart Tool Factory
 * Released under the MIT license
 * SPDX-License-Identifier: MIT
 * https://github.com/SmartToolFactory/Compose-Drawing-App
 * =================================
 * Modified to fit the project's needs.
 * Adapted to Material 3.
 */

package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import m2sdl.lacuillere.data.PathProperties
import m2sdl.lacuillere.ui.theme.DrawBlue
import m2sdl.lacuillere.ui.theme.DrawGreen
import m2sdl.lacuillere.ui.theme.DrawMagenta
import m2sdl.lacuillere.ui.theme.DrawRed
import m2sdl.lacuillere.ui.theme.DrawYellow
import m2sdl.lacuillere.viewmodel.CanvasViewModel
import m2sdl.lacuillere.viewmodel.CanvasViewModel.DrawMode

@Composable
fun DrawingPropertiesMenu(
	modifier: Modifier = Modifier,
	model: CanvasViewModel,
) {
	var properties by model.currentPathProperty

	var showClearDialog by remember { mutableStateOf(false) }
	var showPropertiesDialog by remember { mutableStateOf(false) }
	val currentDrawMode by model.drawMode

	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		FilledIconToggleButton(
			checked = currentDrawMode == DrawMode.Draw,
			onCheckedChange = { model.setDrawMode(DrawMode.Draw) },
		) {
			Icon(Icons.Filled.Edit, contentDescription = null)
		}

		FilledIconToggleButton(
			checked = currentDrawMode == DrawMode.Move,
			onCheckedChange = { model.setDrawMode(DrawMode.Move) },
		) {
			Icon(Icons.Filled.TouchApp, contentDescription = null)
		}

		VerticalDivider(Modifier.padding(vertical = 16.dp))

		IconButton(onClick = { model.undo() }) {
			Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null)
		}

		IconButton(onClick = { model.redo() }) {
			Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = null)
		}

		IconButton(onClick = { showClearDialog = !showClearDialog }) {
			Icon(Icons.Default.Clear, contentDescription = null)
		}

		VerticalDivider(Modifier.padding(vertical = 16.dp))

		IconButton(onClick = { showPropertiesDialog = !showPropertiesDialog }) {
			Icon(Icons.Filled.Tune, contentDescription = null)
		}
	}

	if (showPropertiesDialog) {
		PropertiesMenuDialog(properties, { properties = it }) {
			showPropertiesDialog = !showPropertiesDialog
		}
	}

	if (showClearDialog) {
		AlertDialog(
			icon = { Icon(Icons.Default.Warning, contentDescription = null) },
			title = { Text(text = "Tout effacer ?") },
			text = { Text(text = "Êtes-vous sûr·e de vouloir tout effacer ?") },
			onDismissRequest = { showClearDialog = false },
			confirmButton = {
				TextButton(onClick = { showClearDialog = false; model.clear() }) { Text("Tour effacer") }
			},
			dismissButton = {
				TextButton(onClick = { showClearDialog = false }) { Text("Annuler") }
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertiesMenuDialog(
	pathOption: PathProperties,
	onPathOptionsChange: (PathProperties) -> Unit,
	onDismiss: () -> Unit,
) {
	var strokeWidth by remember { mutableStateOf(pathOption.strokeWidth) }
	var alpha by remember { mutableStateOf(pathOption.alpha) }

	ModalBottomSheet(onDismissRequest = onDismiss) {
		Column(modifier = Modifier.padding(horizontal = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
			Text(
				"Paramètres de dessin",
				style = MaterialTheme.typography.titleLarge,
				modifier = Modifier
					.basicMarquee()
					.padding(bottom = 16.dp)
			)

			Column {
				Text("Couleur", style = MaterialTheme.typography.labelLarge)

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 8.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					ColorButton(pathOption, Color.Black) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, DrawRed) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, DrawYellow) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, DrawGreen) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, DrawBlue) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, DrawMagenta) { onPathOptionsChange(it); onDismiss() }
					ColorButton(pathOption, Color.White) { onPathOptionsChange(it); onDismiss() }
				}
			}

			Column {
				Text("Épaisseur du trait", style = MaterialTheme.typography.labelLarge)

				Slider(
					value = strokeWidth,
					onValueChange = { strokeWidth = it },
					onValueChangeFinished = { onPathOptionsChange(pathOption.copy(strokeWidth = strokeWidth)) },
					valueRange = 1f..100f,
				)
			}

			Column {
				Text("Opacité du trait", style = MaterialTheme.typography.labelLarge)

				Slider(
					value = alpha,
					onValueChange = { alpha = it },
					onValueChangeFinished = { onPathOptionsChange(pathOption.copy(alpha = alpha)) },
					valueRange = 0f..1f,
				)
			}

			HorizontalDivider()

			Text(
				"Paramètres avancés",
				style = MaterialTheme.typography.titleMedium,
				modifier = Modifier
					.basicMarquee()
					.padding(bottom = 16.dp)
			)

			ExposedSelectionMenu(
				title = "Extrémité des traits",
				index = when (pathOption.strokeCap) {
					StrokeCap.Butt -> 0
					StrokeCap.Round -> 1
					else -> 2
				},
				options = listOf("Buttée", "Arrondi", "Carré"),
				onSelected = {
					onPathOptionsChange(
						pathOption.copy(
							strokeCap = when (it) {
								0 -> StrokeCap.Butt
								1 -> StrokeCap.Round
								else -> StrokeCap.Square
							}
						)
					)
				}
			)

			ExposedSelectionMenu(
				title = "Jointure des traits",
				index = when (pathOption.strokeJoin) {
					StrokeJoin.Miter -> 0
					StrokeJoin.Round -> 1
					else -> 2
				},
				options = listOf("Onglet", "Arrondi", "Biseau"),
				onSelected = {
					onPathOptionsChange(
						pathOption.copy(
							strokeJoin = when (it) {
								0 -> StrokeJoin.Miter
								1 -> StrokeJoin.Round
								else -> StrokeJoin.Bevel
							}
						)
					)
				}
			)
		}
	}
}

@Composable
fun ColorButton(pathOption: PathProperties, color: Color, onColorChange: (PathProperties) -> Unit) {
	Button(
		modifier = Modifier
			.width(25.dp)
			.height(25.dp)
			.clip(RoundedCornerShape(13.dp))
			.drawBehind {
				if (pathOption.color == color) {
					drawCircle(
						color = color,
						center = Offset(size.width / 2, size.height / 2),
						radius = 18.dp.toPx(),
						style = Stroke(8f)
					)
				}
			},
		colors = ButtonDefaults.buttonColors(containerColor = color),
		onClick = { onColorChange(pathOption.copy(color = color)) }
	) {}
}

/**
 * Expandable selection menu
 * @param title of the displayed item on top
 * @param index index of selected item
 * @param options list of [String] options
 * @param onSelected lambda to be invoked when an item is selected that returns
 * its index.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedSelectionMenu(
	title: String,
	index: Int,
	options: List<String>,
	onSelected: (Int) -> Unit,
) {
	var expanded by remember { mutableStateOf(false) }
	var selectedOptionText by remember { mutableStateOf(options[index]) }
	var selectedIndex = remember { index }

	ExposedDropdownMenuBox(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		modifier = Modifier.fillMaxWidth(),
	) {
		TextField(
			readOnly = true,
			modifier = Modifier
				.fillMaxWidth()
				.menuAnchor(MenuAnchorType.PrimaryNotEditable),
			value = selectedOptionText,
			onValueChange = {},
			label = { Text(title) },
			trailingIcon = {
				ExposedDropdownMenuDefaults.TrailingIcon(
					expanded = expanded
				)
			}
		)
		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = {
				expanded = false
			}
		) {
			options.forEachIndexed { index: Int, selectionOption: String ->
				DropdownMenuItem(
					contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
					text = { Text(text = selectionOption) },
					onClick = {
						selectedOptionText = selectionOption
						expanded = false
						selectedIndex = index
						onSelected(selectedIndex)
					}
				)
			}
		}
	}
}
