package com.example.loungy.login

import com.example.loungy.dtos.LoginDTO
import com.example.loungy.errors.CustomErrorException
import com.example.loungy.profile.Profiles
import com.example.loungy.profile.recordToProfileCreate
import com.example.loungy.sha256.KnowledgeFactorySHA256.encryptThisString
import org.jooq.DSLContext
import org.jooq.generated.public_.tables.Profile.PROFILE
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Repository


@Repository
class LoginRepository(
    private val dslContext: DSLContext
) {
    fun loginUser(user: LoginDTO): Profiles {

        return checkProfile(user)
            ?: throw CustomErrorException(BAD_REQUEST, "Username or password is incorrect", BAD_REQUEST.value())
    }

    private fun checkProfile(user: LoginDTO): Profiles? {
        return dslContext.select(
            PROFILE.NAME,
            PROFILE.PASSWORD,
            PROFILE.ROLE
        )
            .from(PROFILE)
            .where(PROFILE.NAME.eq(user.username))
            .and(PROFILE.PASSWORD.eq(encryptThisString(user.password)))
            .fetchOne(::recordToProfileCreate)
    }
}