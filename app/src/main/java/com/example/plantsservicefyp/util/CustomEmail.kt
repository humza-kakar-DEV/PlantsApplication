package com.example.plantsservicefyp.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class CustomEmail @Inject constructor(
    @ApplicationContext var context: Context
) {

    private val appEmail: String = "humzakakar200331@gmail.com"
    private val appEmailPassword: String = "humzajavadeveloper"
    private val appPassword: String = "etuzwmuveigdsted"

    suspend fun sendEmail(toEmail: String, emailSubject: String, emailBody: String) {
        withContext(Dispatchers.IO) {
            val prop = Properties()
            prop["mail.smtp.host"] = "smtp.gmail.com"
            prop["mail.smtp.port"] = "587"
            prop["mail.smtp.auth"] = "true"
            prop["mail.smtp.starttls.enable"] = "true" //TLS

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
            } catch (e: Exception) {
                e.printStackTrace()
                context?.log("mail error : ${e.toString()}")
            }
        }
    }

}