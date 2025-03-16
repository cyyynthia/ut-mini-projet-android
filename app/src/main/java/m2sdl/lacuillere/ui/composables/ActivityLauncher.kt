package m2sdl.lacuillere.ui.composables

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun <T : Activity> rememberActivityLauncherForResult(activity: Class<T>, callback: (Intent?) -> Unit): () -> Unit {
	val ctx = LocalContext.current

	val contract = ActivityResultContracts.StartActivityForResult()
	val launcher = rememberLauncherForActivityResult(contract) {
		if (it.resultCode == Activity.RESULT_OK) callback(it.data)
	}

	return { launcher.launch(Intent(ctx, activity)) }
}
