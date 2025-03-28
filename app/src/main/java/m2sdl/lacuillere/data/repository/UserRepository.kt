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

package m2sdl.lacuillere.data.repository

import m2sdl.lacuillere.R
import m2sdl.lacuillere.data.User
import m2sdl.lacuillere.hacks.BitmapOrDrawableRef
import java.util.UUID

class UserRepository(initialData: MutableList<User>?) :
	AbstractRepository<User>(initialData ?: mutableListOf(*BASE_DATA)) {

	fun findMyself() = findById(BOB.id) ?: throw RuntimeException()

	companion object {
		private val BOB = User(
			id = UUID.fromString("7bd006a8-2cc6-46d6-ad8c-d263e8192fe5"),
			name = "Bob le Bricoleur",
			avatar = BitmapOrDrawableRef(R.drawable.bob_le_brico_avatar)
		)

		private val PAYOU = User(
			id = UUID.fromString("520f5c7e-71fd-4adb-8c35-5553cf8bd7c6"),
			name = "Payou",
			avatar = BitmapOrDrawableRef(R.drawable.payou_avatar)
		)

		private val KAYOU = User(
			id = UUID.fromString("31c569ea-5e9b-43b2-94a5-e0a942ba337e"),
			name = "Kayou",
			avatar = BitmapOrDrawableRef(R.drawable.kayou_avatar)
		)

		private val RACAYOU = User(
			id = UUID.fromString("7633aa59-e287-4d81-8b46-13927ee7bde9"),
			name = "Racayou",
			avatar = BitmapOrDrawableRef(-1)
		)

		private val RAYENNE_PARIS = User(
			id = UUID.fromString("53b5b066-1bbb-48c9-a03e-990e352d6057"),
			name = "Rayenne de Paris",
			avatar = BitmapOrDrawableRef(R.drawable.rayenne_de_paris_avatar)
		)

		private val RENE = User(
			id = UUID.fromString("9934b352-6e73-42b7-ab5e-b4b360ffbb85"),
			name = "RENÉ MALLEVILLE",
			avatar = BitmapOrDrawableRef(R.drawable.rene_malleville_avatar)
		)

		private val BASE_DATA = arrayOf<User>(
			BOB,
			PAYOU,
			KAYOU,
			RACAYOU,
			RAYENNE_PARIS,
			RENE,
		)
	}
}
