package com.example.loungy.email

import com.example.loungy.tools.getRandomString
import org.jooq.DSLContext
import org.jooq.generated.public_.tables.EmailConfirm.EMAIL_CONFIRM
import org.jooq.generated.public_.tables.Profile
import org.jooq.generated.public_.tables.Profile.PROFILE
import org.jooq.impl.DSL
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class EmailRepository(
    private val dslContext: DSLContext,
) {
    fun createConfirm(email: String, uniqueKey: String) {

        val now = LocalDateTime.now()

        // VALID_TILL IS NOT HANDLED
        dslContext
            .insertInto(EMAIL_CONFIRM)
            .set(EMAIL_CONFIRM.CREATED_AT, now)
            .set(EMAIL_CONFIRM.VALID_TILL, now.plusSeconds(60 * 60 * 24)) // 24 hours
            .set(EMAIL_CONFIRM.KEY, uniqueKey)
            .set(EMAIL_CONFIRM.EMAIL, email)
            .execute()
    }

    fun confirmEmail(uniqueKey: String) {
        val profileEmail = DSL.name("profileEmail").`as`(
            select(EMAIL_CONFIRM.EMAIL)
                .from(EMAIL_CONFIRM)
                .where(EMAIL_CONFIRM.KEY.eq(uniqueKey))
        )

        dslContext
            .with(profileEmail)
            .update(PROFILE)
            .set(PROFILE.EMAIL_CONFIRM, true)
            .from(profileEmail)
            .where(PROFILE.EMAIL.eq(profileEmail.field(PROFILE.EMAIL)))
            .execute()

    }
}

//val lockable = DSL.name("lockable").`as`(
//    select(ONBOARDING_SYNC_QUEUE.ID)
//        .distinctOn(ONBOARDING_SYNC_QUEUE.PROFILE_ID)
//        .from(ONBOARDING_SYNC_QUEUE)
//        .where()
//        .andFilter(profileIds, ONBOARDING_SYNC_QUEUE.PROFILE_ID::`in`)
//)
//
//return dslContext
//.with(lockable)
//.update(ONBOARDING_SYNC_QUEUE)
//.set(ONBOARDING_SYNC_QUEUE.LOCKED_AT, LocalDateTime.now(clock))
//.from(lockable)
//.where(ONBOARDING_SYNC_QUEUE.ID.eq(lockable.field(ONBOARDING_SYNC_QUEUE.ID)))
//.returning(ONBOARDING_SYNC_QUEUE.ID, ONBOARDING_SYNC_QUEUE.PROFILE_ID)
//.fetch()
//.map {
//    QueueItem(
//        id = it[ONBOARDING_SYNC_QUEUE.ID],
//        profileId = it[ONBOARDING_SYNC_QUEUE.PROFILE_ID],
//    )
//}