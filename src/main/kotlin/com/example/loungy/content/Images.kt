package com.example.loungy.content

import com.example.loungy.profile.Profiles
import org.jooq.Record
import org.jooq.generated.public_.tables.Images.IMAGES
import org.jooq.generated.public_.tables.Profile

data class Images(
    var filename: String?,
    var mimeType: String,
    var data: ByteArray,
)


fun recordImages(r: Record): Images {
    return Images(
        filename = r[IMAGES.FILE_NAME],
        mimeType = r[IMAGES.MIME_TYPE],
        data = r[IMAGES.DATA]
    )
}