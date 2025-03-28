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

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.BundleCompat
import m2sdl.lacuillere.data.Reservation
import m2sdl.lacuillere.data.Restaurant
import m2sdl.lacuillere.data.Review
import m2sdl.lacuillere.data.User


// That's ugly, but it does the trick. cba to get real DI up and running
object RepositoryLocator {
	private lateinit var userRepository: UserRepository
	private lateinit var restaurantRepository: RestaurantRepository
	private lateinit var reviewRepository: ReviewRepository
	private lateinit var reservationRepository: ReservationRepository

	fun getUserRepository() = userRepository
	fun getRestaurantRepository() = restaurantRepository
	fun getReviewRepository() = reviewRepository
	fun getReservationRepository() = reservationRepository

	fun init(ctx: Context) {
		// it's... yeah. dw about it.
		// it is what it is.

		val parcel = Parcel.obtain()
		var out: Bundle? = null
		try {
			val fis = ctx.openFileInput("appdata.bin")
			val array = ByteArray(fis.getChannel().size().toInt())
			fis.read(array, 0, array.size)
			fis.close()
			parcel.unmarshall(array, 0, array.size)
			parcel.setDataPosition(0)
			out = parcel.readBundle(this::class.java.getClassLoader())
		} catch (e: Throwable) {
			println(e)
			ctx.deleteFile("appdata.bin")
		} finally {
			parcel.recycle()
			init(out)
		}
	}

	private fun init(bundle: Bundle?) {
		// rofl
		val user = bundle?.getParcelableMutableList<User>("user")
		val resto = bundle?.getParcelableMutableList<Restaurant>("resto")
		val review = bundle?.getParcelableMutableList<Review>("review")
		val rsv = bundle?.getParcelableMutableList<Reservation>("rsv")

		println(bundle)

		userRepository = UserRepository(user)
		restaurantRepository = RestaurantRepository(resto)
		reviewRepository = ReviewRepository(review)
		reservationRepository = ReservationRepository(rsv)
	}

	fun purge(ctx: Context) {
		ctx.deleteFile("appdata.bin")
		userRepository = UserRepository(null)
		restaurantRepository = RestaurantRepository(null)
		reviewRepository = ReviewRepository(null)
		reservationRepository = ReservationRepository(null)
	}

	fun save(ctx: Context) {
		// it's... yeah. dw about it.
		// it is what it is.

		val bundle = Bundle()
		bundle.putParcelableArray("user", userRepository.findAll().toTypedArray())
		bundle.putParcelableArray("resto", restaurantRepository.findAll().toTypedArray())
		bundle.putParcelableArray("review", reviewRepository.findAll().toTypedArray())
		bundle.putParcelableArray("rsv", reservationRepository.findAll().toTypedArray())

		ctx.openFileOutput("appdata.bin", Context.MODE_PRIVATE).use {
			val parcel = Parcel.obtain()
			try {
				// "As such, it is not appropriate to place any Parcel data in to persistent storage"
				// HOLD MY BEER
				bundle.writeToParcel(parcel, 0)
				it.write(parcel.marshall())
				it.flush()
				it.close()
			} catch (e: Throwable) {
				println(e)
			} finally {
				parcel.recycle()
			}
		}
	}

	private inline fun <reified T : Parcelable> Bundle.getParcelableMutableList(key: String): MutableList<T>? {
		try {
			@Suppress("UNCHECKED_CAST")
			val array = BundleCompat.getParcelableArray(this, key, T::class.java) as Array<T>
			return array.toMutableList()
		} catch (e: Throwable) {
			println(e)
			remove(key)
			return null
		}
	}
}
