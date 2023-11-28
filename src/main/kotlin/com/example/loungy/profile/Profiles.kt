package com.example.loungy.profile

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotBlank
import org.jooq.Record
import org.jooq.generated.public_.tables.Profile.PROFILE

data class Profiles(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    @field: JsonIgnore
    val password: String,

    @field:NotBlank
    val role: String? = "USER"
)

fun recordToProfileCreate(r: Record): Profiles {
    return Profiles(
        username = r[PROFILE.NAME],
        password = r[PROFILE.PASSWORD],
        role = r[PROFILE.ROLE]
    )
}

data class Role(
    val role: String
)

fun recordToRole(r: Record): Role {
    return Role(
        role = r[PROFILE.ROLE]
    )
}