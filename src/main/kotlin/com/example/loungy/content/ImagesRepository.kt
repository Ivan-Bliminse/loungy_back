package com.example.loungy.content

import org.jooq.DSLContext
import org.jooq.generated.public_.tables.Images.IMAGES
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile
import com.example.loungy.content.Images

@Repository
class ImagesRepository(
    private val dslContext: DSLContext
) {
    fun get(): List<Images> {
        return dslContext
            .select(*IMAGES.fields())
            .from(IMAGES)
            .fetch(::recordImages)
    }

    fun upload(image: MultipartFile): Boolean {
//        val now = LocalDateTime.now()

        return dslContext.insertInto(IMAGES)
            .set(IMAGES.FILE_NAME, image.originalFilename)
            .set(IMAGES.MIME_TYPE, image.contentType)
            .set(IMAGES.DATA, image.bytes)
            .execute() == 1
    }
}

