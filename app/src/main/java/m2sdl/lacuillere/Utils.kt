package m2sdl.lacuillere

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.maps.android.compose.CameraPositionState

fun checkPermissions(ctx: Context, vararg permissions: String): Boolean {
	return permissions.any { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }
}

fun CameraPositionState.isNull() = position.target.latitude == 0.0 && position.target.longitude == 0.0
