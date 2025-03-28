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

package m2sdl.lacuillere.data

import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.time.LocalTime
import java.util.UUID

@Parcelize
data class Restaurant(
	override val id: UUID = UUID.randomUUID(),
	val name: String,
	val about: String,
	val address: String,
	val addressShort: String,
	val telephone: String,
	val openingHours: List<Pair<LocalTime, LocalTime>>,
	val menu: String,
	val banner: BitmapOrDrawableRef,
	val photos: List<BitmapOrDrawableRef>,
	val position: LatLng,
) : Entity {
	fun isCurrentlyOpen(): Boolean {
		val lt = LocalTime.now()
		return openingHours.any {
			if (it.first > it.second) {
				// Transition over midnight. Think the other way around; check we're NOT inside the first--end range
				!(it.first >= lt && it.second <= lt)
			} else {
				it.first >= lt && it.second <= lt
			}
		}
	}

	fun nextOpeningHours(): LocalTime {
		// Deals with transitions over midnight. KISS
		if (openingHours.size == 1) return openingHours[0].first

		val lt = LocalTime.now()
		return openingHours.firstOrNull { it.first < lt }?.first
			?: openingHours.first().first
	}

	fun nextClosingHours(): LocalTime {
		// Deals with transitions over midnight. KISS
		if (openingHours.size == 1) return openingHours[0].second

		val lt = LocalTime.now()
		return openingHours.firstOrNull { it.first >= lt }?.second
			?: openingHours.first().second // This would definitely be a suspicious case... cba to deal with it properly.
	}
}
