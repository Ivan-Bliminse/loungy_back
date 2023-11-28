package com.example.loungy.email

import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/v1/emailConfirm")
class EmailController(
    private val emailRepository: EmailRepository
) {
    @GetMapping("/{uniqueKey}")
    fun confirmEmail(@PathVariable uniqueKey: String): Boolean {
        emailRepository.confirmEmail(uniqueKey)

        return true
    }
}

/*
    Register - submit
    create record in Profile table, column email_confirmed = false
    create record in EmailConfirmed table, column key (generated key) :: id, created_at, valid_till, key, email
    Send email with link to confirm email
    Profile table, column email_confirmed = true
 */