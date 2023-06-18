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

//    private val appEmail: String = "kamilkakar123456@gmail.com"
//    private val appEmailPassword: String = "humzajavadeveloper"
//    private val appPassword: String = "mhyuhtaccefbjgzo"

    private val appEmail: String = "humzakakar200331@gmail.com"
    private val appEmailPassword: String = "humzajavadeveloper"
    private val appPassword: String = "vlmlqjmiiohuhjjx"

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

//    var host = "mail.javatpoint.com"
//    val user = "hamzakakar200331@gmail.com" //change accordingly
//    val password = "vlmlqjmiiohuhjjx" //change accordingly
//    var to = "hamza.coding.world@gmail.com" //change accordingly

//    suspend fun sendMail(toEmail: String, emailSubject: String, emailBody: String) {
//        withContext(Dispatchers.IO) {
//            val username = "humzakakar200331@gmail.com"
//            val password = "vlmlqjmiiohuhjjx"
//
//            val prop = Properties()
//            prop["mail.smtp.host"] = "smtp.gmail.com"
//            prop["mail.smtp.port"] = "587"
//            prop["mail.smtp.auth"] = "true"
//            prop["mail.smtp.starttls.enable"] = "true" //TLS
//
//
//            val session = Session.getInstance(prop,
//                object : Authenticator() {
//                    override fun getPasswordAuthentication(): PasswordAuthentication {
//                        return PasswordAuthentication(username, password)
//                    }
//                })
//
//            try {
//                val message: Message = MimeMessage(session)
//                message.setFrom(InternetAddress(username))
//                message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(toEmail)
//                )
//                message.subject = emailSubject
//                message.setText(emailBody)
//                Transport.send(message)
//                println("Done")
//                context?.log("email success")
//            } catch (e: MessagingException) {
//                e.printStackTrace()
//                context?.log("email error: ${e.message}")
//            }
//        }
//    }

}