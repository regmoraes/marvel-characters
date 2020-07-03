package br.com.regmoraes.marvelcharacters.presentation.model

import android.os.Parcelable
import br.com.regmoraes.marvelcharacters.model.Thumbnail
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThumbnailParcel(val path: String, val extension: String) : Parcelable {
    val pathWithExtension: String
        get() {
            val httpsPath = path.replaceFirst("http:", "https:")
            return "$httpsPath.$extension"
        }
}

fun Thumbnail.toParcel(): ThumbnailParcel = ThumbnailParcel(path, extension)
fun ThumbnailParcel.toDomain(): Thumbnail = Thumbnail(path, extension)