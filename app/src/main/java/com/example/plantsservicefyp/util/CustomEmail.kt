package com.example.plantsservicefyp.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class CustomEmail constructor(
    var context: Context
) {

    private val appEmail: String = "PlanTea.366@gmail.com"
    private val appEmailPassword: String = "JKdkjl92309218_*()()ksljdfJLKJ"
    private val appPassword: String = "kexmqdptkfeukjvm"

    suspend fun sendEmail(toEmail: String, emailSubject: String, emailBody: String, callback: () -> Unit) {
        withContext(Dispatchers.IO) {
            val prop = Properties()
            prop["mail.smtp.host"] = "smtp.gmail.com"
            prop["mail.smtp.port"] = "587"
            prop["mail.smtp.auth"] = "true"
            prop["mail.smtp.starttls.enable"] = "true"

            val session = Session.getInstance(prop,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(appEmail, appPassword)
                    }
                })

            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(appEmail))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
                )
                message.subject = emailSubject
                message.setText(emailBody)

                Transport.send(message)

                callback()


            } catch (e: Exception) {
                context?.log("mail error : ${e.toString()}")
                e.printStackTrace()
            }
        }
    }

}