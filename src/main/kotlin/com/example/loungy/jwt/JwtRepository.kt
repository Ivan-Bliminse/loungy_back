package com.example.loungy.jwt

import com.example.loungy.errors.CustomErrorException
import org.jooq.DSLContext
import org.jooq.generated.public_.tables.Token
import org.jooq.generated.public_.tables.Token.TOKEN
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JwtRepository(
    val dslContext: DSLContext,
) {
    fun insert(
        token: String,
        signingKey: String,
    ): Boolean {
        val now = LocalDateTime.now()

        return dslContext
            .insertInto(TOKEN)
            .set(TOKEN.TOKEN_, token)
            .set(TOKEN.SIGNING_KEY, signingKey)
            .set(TOKEN.VALID_TILL, now.plusSeconds(60 * 60 * 24))
            .set(TOKEN.CREATED_AT, now)
            .execute() == 1
    }

    fun getActiveSigningKey(
        token: String,
    ): String =
        dslContext
            .select(TOKEN.SIGNING_KEY)
            .from(TOKEN)
            .where(TOKEN.TOKEN_.eq(token))
            .and(TOKEN.VALID_TILL.ge(LocalDateTime.now()))
            .fetchOne(TOKEN.SIGNING_KEY)
            ?: throw CustomErrorException(UNAUTHORIZED, "Token is invalid or expired", UNAUTHORIZED.value())

    fun logout(
        token: String
    ): Boolean =
        dslContext
            .update(TOKEN)
            .set(TOKEN.VALID_TILL, LocalDateTime.now())
            .where(TOKEN.TOKEN_.eq(token))
            .execute() == 1

}