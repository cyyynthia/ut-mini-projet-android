package m2sdl.lacuillere.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.os.BundleCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import m2sdl.lacuillere.PhotoActivity
import m2sdl.lacuillere.asBitmap
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.repository.RepositoryLocator
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef.Companion.toHackyBitmapList
import m2sdl.lacuillere.hideKeyboardOnOutsideClick
import m2sdl.lacuillere.toast
import m2sdl.lacuillere.ui.components.BackButton
import m2sdl.lacuillere.ui.components.LinearLoadingOverlay
import m2sdl.lacuillere.ui.components.RestoCard
import m2sdl.lacuillere.ui.composables.rememberActivityLauncherForResult
import kotlin.time.Duration.Companion.seconds

private enum class ReviewScreenState {
	Filling,
	Submitting,
	Success,
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RestoReviewScreen(restaurant: Restaurant, onBack: () -> Unit) {
	val ctx = LocalContext.current
	val keyboardController = LocalSoftwareKeyboardController.current

	val scope = rememberCoroutineScope()
	var state by rememberSaveable { mutableStateOf(ReviewScreenState.Filling) }

	var note by rememberSaveable { mutableStateOf(5f) }
	var comment by rememberSaveable { mutableStateOf("") }
	val images = remember { mutableStateListOf<Bitmap>() } // data loss is likely, but I LITERALLY DON'T CARE ANYMORE

	val photoActivityLauncher = rememberActivityLauncherForResult(PhotoActivity::class.java) {
		val bitmap = it?.extras?.let { BundleCompat.getParcelable(it, "photo", ByteArray::class.java) }?.asBitmap()
		bitmap?.let { images.add(bitmap) }
	}

	// Should be tucked in a view model or smth. eh.
	// We also don't do validation... which shouldn't be too hard to do
	// It's mostly a few derivedStateOf and adding error to fields :shrug:
	fun doSubmit() {
		state = ReviewScreenState.Submitting

		scope.launch {
			delay(2.seconds)
			val me = RepositoryLocator.getUserRepository().findMyself()
			RepositoryLocator.getReviewRepository().insert(
				Review(
					userId = me.id,
					restaurantId = restaurant.id,
					note = note,
					text = comment,
					photos = images.toHackyBitmapList(),
				)
			)

			ctx.toast("Votre avis a été publié avec succès.")
			state = ReviewScreenState.Success
			onBack()
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				navigationIcon = {
					BackButton(onBack = onBack, contentDescription = "Retour")
				},
				title = { Text("Laisser un avis") }
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(16.dp)
				.verticalScroll(rememberScrollState())
				.hideKeyboardOnOutsideClick(),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			RestoCard(restaurant)

			Text(
				"Le contenu que vous postez ici est public et soumis aux règles communautaires. Veillez à rester courtois envers les restaurateurs.",
				style = MaterialTheme.typography.bodySmall
			)

			Column {
				Text("Note", style = MaterialTheme.typography.labelLarge)

				Slider(
					value = note,
					onValueChange = { note = it },
					valueRange = 1f..5f,
					steps = 3,
				)
			}

			OutlinedTextField(
				value = comment,
				onValueChange = { comment = it },
				label = { Text("Commentaire") },
				modifier = Modifier.fillMaxWidth(),
				keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
				minLines = 4,
			)

			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				Text("Photos", style = MaterialTheme.typography.labelLarge)

				if (images.isNotEmpty()) {
					FlowRow(
						verticalArrangement = Arrangement.spacedBy(8.dp),
						horizontalArrangement = Arrangement.spacedBy(8.dp),
					) {
						images.forEach {
							Image(
								it.asImageBitmap(),
								contentDescription = null,
								modifier = Modifier
									.clip(RoundedCornerShape(8.dp))
									.width(120.dp)
							)
						}
					}
				}

				Button(onClick = photoActivityLauncher) {
					Text("Prendre une photo")
				}
			}

			Spacer(Modifier.weight(1f))

			Button(
				modifier = Modifier.fillMaxWidth(),
				enabled = state == ReviewScreenState.Filling,
				onClick = { doSubmit() }
			) {
				Text("Envoyer")
			}
		}

		LinearLoadingOverlay(
			state == ReviewScreenState.Submitting,
			Modifier.padding(innerPadding)
		)
	}
}
