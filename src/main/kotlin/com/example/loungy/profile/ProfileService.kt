package com.example.loungy.profile

import org.jooq.DSLContext
import org.jooq.generated.public_.tables.Profile
import org.jooq.generated.public_.tables.Profile.PROFILE
import org.springframework.stereotype.Service

@Service
class ProfileService(
    private val dslContext: DSLContext
) {
    fun getRole(username: String): Role? {
        return dslContext.select(PROFILE.ROLE)
            .from(PROFILE)
            .where(PROFILE.NAME.eq(username))
            .fetchOne(::recordToRole)
    }

}