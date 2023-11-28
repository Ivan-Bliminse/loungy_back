package com.example.loungy.dtos

import org.springframework.http.HttpStatus

data class LoginDTO(
    val username: String = "",
    val password: String = ""
)

data class SuccessLoginDTO(
    val status: HttpStatus = HttpStatus.OK,
    val code: Int = 200,
    val message: String = "Success",
    val token: String,
    val role: String?
)