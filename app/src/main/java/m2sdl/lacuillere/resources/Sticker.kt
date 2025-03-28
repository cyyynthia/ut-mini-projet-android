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

package m2sdl.lacuillere.resources

import androidx.annotation.DrawableRes
import m2sdl.lacuillere.R

enum class Sticker(@DrawableRes val resource: Int) {
	BlackHeart(R.drawable.msft_fluent_black_heart_3d),
	BlueHeart(R.drawable.msft_fluent_blue_heart_3d),
	BrownHeart(R.drawable.msft_fluent_brown_heart_3d),
	CherryBlossom(R.drawable.msft_fluent_cherry_blossom_3d),
	ColdFace(R.drawable.msft_fluent_cold_face_3d),
	Dizzy(R.drawable.msft_fluent_dizzy_3d),
	DroolingFace(R.drawable.msft_fluent_drooling_face_3d),
	FaceHoldingBackTears(R.drawable.msft_fluent_face_holding_back_tears_3d),
	FaceSavoringFood(R.drawable.msft_fluent_face_savoring_food_3d),
	FaceWithHandOverMouth(R.drawable.msft_fluent_face_with_hand_over_mouth_3d),
	FaceWithSteamFromNose(R.drawable.msft_fluent_face_with_steam_from_nose_3d),
	FaceWithSymbolsOnMouth(R.drawable.msft_fluent_face_with_symbols_on_mouth_3d),
	Fire(R.drawable.msft_fluent_fire_3d),
	GreenHeart(R.drawable.msft_fluent_green_heart_3d),
	GreyHeart(R.drawable.msft_fluent_grey_heart_3d),
	HeartEyes(R.drawable.msft_fluent_heart_eyes_3d),
	Hibiscus(R.drawable.msft_fluent_hibiscus_3d),
	HotSprings(R.drawable.msft_fluent_hot_springs_3d),
	HundredPoints(R.drawable.msft_fluent_hundred_points_3d),
	LightBlueHeart(R.drawable.msft_fluent_light_blue_heart_3d),
	MoneyWithWings(R.drawable.msft_fluent_money_with_wings_3d),
	NauseatedFace(R.drawable.msft_fluent_nauseated_face_3d),
	OkHand(R.drawable.msft_fluent_ok_hand_3d_default),
	OrangeHeart(R.drawable.msft_fluent_orange_heart_3d),
	PartyPopper(R.drawable.msft_fluent_party_popper_3d),
	PinkHeart(R.drawable.msft_fluent_pink_heart_3d),
	Poop(R.drawable.msft_fluent_poop_3d),
	PoutingFace(R.drawable.msft_fluent_pouting_face_3d),
	PurpleHeart(R.drawable.msft_fluent_purple_heart_3d),
	RedHeart(R.drawable.msft_fluent_red_heart_3d),
	RevolvingHearts(R.drawable.msft_fluent_revolving_hearts_3d),
	ShushingFace(R.drawable.msft_fluent_shushing_face_3d),
	SlightlySmilingFace(R.drawable.msft_fluent_slightly_smiling_face_3d),
	SmilingFaceWithHalo(R.drawable.msft_fluent_smiling_face_with_halo_3d),
	SmilingFaceWithHearts(R.drawable.msft_fluent_smiling_face_with_hearts_3d),
	Sparkles(R.drawable.msft_fluent_sparkles_3d),
	Spoon(R.drawable.msft_fluent_spoon_3d),
	StarStruck(R.drawable.msft_fluent_star_struck_3d),
	Thermometer(R.drawable.msft_fluent_thermometer_3d),
	ThumbsDown(R.drawable.msft_fluent_thumbs_down_3d_default),
	ThumbsUp(R.drawable.msft_fluent_thumbs_up_3d_default),
	WhiteHeart(R.drawable.msft_fluent_white_heart_3d),
	YellowHeart(R.drawable.msft_fluent_yellow_heart_3d);

	companion object {
		val SORTED = listOf(
			SlightlySmilingFace,
			SmilingFaceWithHalo,
			SmilingFaceWithHearts,
			HeartEyes,
			StarStruck,
			FaceSavoringFood,
			FaceWithHandOverMouth,
			ShushingFace,
			DroolingFace,
			NauseatedFace,
			ColdFace,
			FaceHoldingBackTears,
			FaceWithSteamFromNose,
			PoutingFace,
			FaceWithSymbolsOnMouth,
			Poop,
			RevolvingHearts,
			RedHeart,
			PinkHeart,
			OrangeHeart,
			YellowHeart,
			GreenHeart,
			BlueHeart,
			LightBlueHeart,
			PurpleHeart,
			BrownHeart,
			BlackHeart,
			GreyHeart,
			WhiteHeart,
			HundredPoints,
			Dizzy,
			OkHand,
			ThumbsUp,
			ThumbsDown,
			CherryBlossom,
			Hibiscus,
			HotSprings,
			Thermometer,
			Fire,
			Sparkles,
			PartyPopper,
			MoneyWithWings,
			Spoon,
		)
	}
}
