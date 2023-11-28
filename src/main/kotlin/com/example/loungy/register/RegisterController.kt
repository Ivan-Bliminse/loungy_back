package com.example.loungy.register

import com.example.loungy.email.EmailRepository
import com.example.loungy.email.EmailService
import com.example.loungy.tools.getRandomString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/register")
@CrossOrigin
class RegisterController(
    private val userRepository: UserRepository,
    private val emailRepository: EmailRepository,
    private val emailService: EmailService

) {

    @PostMapping
    fun registerUser(@RequestBody newUser: NewUser): ResponseEntity<NewUser> {
        val uniqueKey = getRandomString(24)

        emailRepository.createConfirm(newUser.email, uniqueKey)

//        emailService.sendEmail(newUser.email, "http://localhost:8080/api/v1/emailConfirm/$uniqueKey")

        return ResponseEntity.ok(userRepository.createUser(newUser))
    }
}