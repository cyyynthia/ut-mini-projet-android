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

package m2sdl.lacuillere.hacks

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.core.graphics.drawable.toBitmap
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.asBitmap
import m2sdl.lacuillere.asCompressedByteArray
import m2sdl.lacuillere.data.repository.RepositoryLocator

@Parcelize
class BitmapOrDrawableRef private constructor(
	@IgnoredOnParcel
	private var bitmap: Bitmap? = null,
	private val drawable: Int? = null,
	private var compressedBitmap: ByteArray? = null,
) : Parcelable {
	companion object {
		fun Bitmap.toHackyBitmap(): BitmapOrDrawableRef {
			return BitmapOrDrawableRef(this)
		}

		fun Iterable<Bitmap>.toHackyBitmapList(): List<BitmapOrDrawableRef> {
			return map { it.toHackyBitmap() }
		}
	}

	constructor(bitmap: Bitmap) : this(bitmap, null)
	constructor(@DrawableRes drawable: Int) : this(null, drawable)

	init {
		compressedBitmap?.let { bitmap = it.asBitmap() }
			?: bitmap?.let { compressedBitmap = it.asCompressedByteArray() }
	}

	@IgnoredOnParcel
	private lateinit var cachedBitmap: Bitmap

	fun toBitmap(ctx: Context): Bitmap {
		if (!this::cachedBitmap.isInitialized) {
			cachedBitmap = bitmap
				?: drawable?.let { ctx.resources.getDrawable(it, null).toBitmap() }
					?: throw IllegalStateException()
		}

		return cachedBitmap
	}

	@Composable
	fun rememberBitmap(): Bitmap {
		val ctx = LocalContext.current
		return remember(this, ctx) {
			try {
				toBitmap(ctx)
			} catch (t: Throwable) {
				// Likely fucked smth up in dev
				// Kind of extreme solution but at that point...
				RepositoryLocator.purge(ctx)
				throw t
			}
		}
	}

	@Composable
	fun HackyImage(
		contentDescription: String?,
		fitIn: DpSize? = null,
		modifier: Modifier = Modifier,
		contentScale: ContentScale = ContentScale.Crop,
	) {
		val ctx = LocalContext.current
		val bitmap = rememberBitmap()

		val scaledBitmap = fitIn?.let {
			with(Density(ctx)) {
				bitmap.resizeToFitIn(
					it.width.roundToPx(),
					it.height.roundToPx(),
				)
			}
		} ?: bitmap

		Image(
			scaledBitmap.asImageBitmap(),
			contentDescription = contentDescription,
			modifier = modifier,
			contentScale = contentScale,
		)
	}

	private fun Bitmap.resizeToFitIn(targetWidth: Int, targetHeight: Int): Bitmap {
		if (width > height) {
			if (height < targetHeight) return this
			val targetWidth = (width * targetHeight / height)
			return Bitmap.createScaledBitmap(
				this,
				targetWidth,
				targetHeight,
				true
			)
		}

		if (width < targetWidth) return this
		val targetHeight = (height * targetWidth / width)
		return Bitmap.createScaledBitmap(
			this,
			targetWidth,
			targetHeight,
			true
		)
	}
}
