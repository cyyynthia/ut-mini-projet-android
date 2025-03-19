package m2sdl.lacuillere.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun FieldWithDialog(
	value: String,
	label: @Composable () -> Unit,
	placeholder: @Composable () -> Unit,
	trailingIcon: @Composable () -> Unit,
	enabled: Boolean = true,
	modifier: Modifier = Modifier,
	content: @Composable (onDismiss: () -> Unit) -> Unit,
) {
	var showModal by remember { mutableStateOf(false) }

	OutlinedTextField(
		label = label,
		value = value,
		readOnly = true,
		enabled = enabled,
		onValueChange = {},
		placeholder = placeholder,
		trailingIcon = trailingIcon,
		modifier = modifier
			.fillMaxWidth()
			.pointerInput(value) {
				awaitEachGesture {
					// Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
					// in the Initial pass to observe events before the text field consumes them
					// in the Main pass.
					awaitFirstDown(pass = PointerEventPass.Initial)
					val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
					if (upEvent != null) {
						showModal = true
					}
				}
			}
	)

	if (showModal) {
		content { showModal = false }
	}
}
