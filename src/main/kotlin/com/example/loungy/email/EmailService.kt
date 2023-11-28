package com.example.loungy.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailSender: JavaMailSender,
    private val template: SimpleMailMessage,
    @Value("classpath:file.txt")
    private val resourceFile: Resource
) {

    fun sendEmail(
        toEmail: String,
        body: String,
    ) {
        val confirm = SimpleMailMessage()
        confirm.from = "alphaxprogramming@gmail.com"
        confirm.setTo(toEmail)
        confirm.text = body
        confirm.subject = "Email confirmation"
        emailSender.send(confirm)
    }

    fun sendEmailUsingTemplate(
        name: String,
        targetEmail: String
    ) {
        val message = SimpleMailMessage(template)

        val text = template.text
        message.setText(text!!.format(name))
        message.setTo(targetEmail)

        emailSender.send(message)
    }
//
//    fun sendEmailWithAttachment(
//        targetEmail: String
//    ) {
//        val message: MimeMessage = emailSender.createMimeMessage()
//        val helper = MimeMessageHelper(message, true)
//
//        helper.setTo(targetEmail)
//        helper.setSubject("Email with attachment")
//        helper.setText("Please see the file in attachment")
//        helper.addAttachment("file.txt", resourceFile)
//
//        emailSender.send(message)
//    }
}