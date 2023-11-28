package com.example.loungy.sha256

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object KnowledgeFactorySHA256 {
    fun encryptThisString(input: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val messageDigest = md.digest(input.toByteArray())
//            put this in to te bytea::password column
            val no = BigInteger(1, messageDigest)
//            [ No need below ]
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
            hashtext
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
// No need to convert to string, save in bytes