package com.example.loungy.profile

import com.example.loungy.dtos.RoleDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/profile")
@CrossOrigin
@RestController
class ProfileController(
    val profileService: ProfileService
) {
    @GetMapping("/role/{username}")
    fun getUserRole(@PathVariable username: String): ResponseEntity<Role?> {
        return ResponseEntity.ok(profileService.getRole(username))
    }
}