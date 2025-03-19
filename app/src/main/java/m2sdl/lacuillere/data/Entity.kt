package m2sdl.lacuillere.data

import android.os.Parcelable
import java.util.UUID

interface Entity : Parcelable {
	val id: UUID
}
