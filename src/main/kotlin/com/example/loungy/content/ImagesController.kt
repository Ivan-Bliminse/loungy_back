package com.example.loungy.content

import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@CrossOrigin
@RequestMapping("api/v1/images")
class ImagesController(
    private val imagesRepository: ImagesRepository
) {
    @GetMapping
    fun get(): List<Images> {
        return imagesRepository.get()
    }

    @PostMapping
    fun upload(@RequestPart image: MultipartFile): Boolean {
        return imagesRepository.upload(image)
    }

    @PostMapping("/multi")
    fun upload(@RequestParam("images") images: List<MultipartFile>): Boolean {
        images.forEach { imagesRepository.upload(it) }
        return true
    }
}