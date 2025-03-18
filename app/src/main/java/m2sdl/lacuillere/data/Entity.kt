package m2sdl.lacuillere.data

import android.os.Parcelable
import m2sdl.lacuillere.SUUID
import java.util.UUID

sealed class Entity : Parcelable {
	val id: SUUID = UUID.randomUUID()
}
