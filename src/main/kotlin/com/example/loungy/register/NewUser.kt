package com.example.loungy.register

import com.example.loungy.register.ROLES.USER
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.UniqueElements

enum class ROLES {
    USER,
    ADMIN;
}


data class NewUser(
    @field:NotBlank
    @field:UniqueElements
    val username: String,

    @field:NotBlank
//    @field:JsonIgnore
    val password: String,

    @field:NotBlank
    val email: String,

//    val email_confirm: Boolean

    val role: ROLES = USER
)