package m2sdl.lacuillere.data

import android.os.Parcelable
import java.util.UUID

sealed class Entity : Parcelable {
	val id: UUID = UUID.randomUUID()
}
