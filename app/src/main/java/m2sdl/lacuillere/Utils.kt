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

package m2sdl.lacuillere

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.common.util.concurrent.ListenableFuture
import com.google.maps.android.compose.CameraPositionState
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor

fun checkPermissions(ctx: Context, vararg permissions: String): Boolean {
	return permissions.any { ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED }
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
	Toast.makeText(this, text, duration).show()
}

fun Context.notImplementedToast() {
	toast("Cette fonctionnalité n'est pas implémentée.", Toast.LENGTH_SHORT)
}

fun CameraPositionState.isNull() = position.target.latitude == 0.0 && position.target.longitude == 0.0

fun LatLng.applyTo(cps: CameraPositionState) {
	cps.position = CameraPosition.fromLatLngZoom(this, 15f)
}

// Write things in a more Kotlin-friendly way
fun <V> ListenableFuture<V>.addListener(executor: Executor, fn: () -> Unit) = this.addListener(fn, executor)

// "implements Parcelable" my ass
fun Bitmap.asCompressedByteArray(): ByteArray {
	val os = ByteArrayOutputStream()
	compress(Bitmap.CompressFormat.JPEG, 90, os)
	return os.toByteArray()
}

fun ByteArray.asBitmap(): Bitmap {
	return BitmapFactory.decodeByteArray(this, 0, this.size)
}

// Thank you https://stackoverflow.com/a/77939629
fun Modifier.hideKeyboardOnOutsideClick(): Modifier = composed {
	val controller = LocalSoftwareKeyboardController.current
	this then Modifier.noRippleClickable {
		controller?.hide()
	}
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
	this then Modifier.clickable(
		indication = null,
		interactionSource = remember { MutableInteractionSource() },
		onClick = onClick
	)
}
