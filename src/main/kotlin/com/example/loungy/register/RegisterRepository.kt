package com.example.loungy.register

import com.example.loungy.errors.CustomErrorException
import com.example.loungy.sha256.KnowledgeFactorySHA256.encryptThisString
import com.example.loungy.register.ROLES.USER
import org.jooq.DSLContext
import org.jooq.generated.public_.tables.Profile.PROFILE
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository

@Repository
data class UserRepository(
    val dslContext: DSLContext,
) {
    fun createUser(newUser: NewUser): NewUser {
        val checkForUser = getUser(newUser.username)

        if (checkForUser) {
            throw CustomErrorException(
                HttpStatus.NOT_ACCEPTABLE,
                "User with username: ${newUser.username} already exists",
                HttpStatus.NOT_ACCEPTABLE.value()
            )
        }
        createNewUser(newUser)

        return newUser
    }
}

fun UserRepository.getUser(name: String): Boolean {
    return dslContext.select(*PROFILE.fields())
        .from(PROFILE)
        .where(PROFILE.NAME.eq(name))
        .execute() == 1
}

fun UserRepository.createNewUser(newUser: NewUser) {
    dslContext
        .insertInto(PROFILE)
        .set(PROFILE.NAME, newUser.username)
        .set(PROFILE.PASSWORD, encryptThisString(newUser.password))
        .set(PROFILE.EMAIL, newUser.email)
        .set(PROFILE.EMAIL_CONFIRM, false)
        .set(PROFILE.ROLE, USER.name)
        .execute()
}