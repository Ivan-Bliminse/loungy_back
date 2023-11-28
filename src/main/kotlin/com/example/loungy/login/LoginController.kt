package com.example.loungy.login

import com.example.loungy.dtos.LoginDTO
import com.example.loungy.dtos.SuccessLoginDTO
import com.example.loungy.dtos.TokenDTO
import com.example.loungy.jwt.JwtRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
class LoginController(
    private val loginRepository: LoginRepository,
    private val jwtRepository: JwtRepository
) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: LoginDTO): ResponseEntity<SuccessLoginDTO> {
        val signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        val rawData = signingKey.encoded
        val encodedKey = Base64.getEncoder().encodeToString(rawData)

        val issuer = loginRepository.loginUser(user).username
        val role = loginRepository.loginUser(user).role

        val token = Jwts.builder()
//            .setSubject(authenticationId)
            .setIssuer(issuer)
//            .setExpiration(Date(System.currentTimeMillis() + 30000))
            .signWith(signingKey)
            .compact()

        jwtRepository.insert(
            token = token,
            signingKey = encodedKey,
        )

        return ResponseEntity.ok(SuccessLoginDTO(token = token, role = role))
    }

    @PostMapping("/user")
    fun getAuthenticationId(@RequestBody token: TokenDTO): ResponseEntity<Any> {
        val signingKey = jwtRepository.getActiveSigningKey(token.token)

        val body = Jwts.parserBuilder()
            .requireIssuer(token.username)
            .setSigningKey(Base64.getDecoder().decode(signingKey))
            .build()
            .parseClaimsJws(token.token)
            .body

        return ResponseEntity.ok(body)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody token: TokenDTO): ResponseEntity<Boolean> {
        return ResponseEntity.ok(jwtRepository.logout(token.token))
    }
}

