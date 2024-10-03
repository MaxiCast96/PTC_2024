package ptc.proyecto.estrella.bella.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

suspend fun enviarCorreo(receptor: String, asunto: String, mensaje: String) = withContext(Dispatchers.IO){

    val props = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.socketFactory.port", "465")
        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        put("mail.smtp.auth", "true")
        put("mail.smtp.port", "465")
    }

    val session = Session.getInstance(props, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(
                "cinemanow2024@gmail.com",
                "ynen gnki pkps bgqs"
            )
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("cinemanow2024@gmail.com"))
            addRecipient(Message.RecipientType.TO, InternetAddress(receptor))
            subject = asunto
            setContent(mensaje, "text/html; charset=utf-8")
        }
        Transport.send(message)
        println("Correo enviado satisfactoriamente")
    } catch (e: MessagingException) {
        e.printStackTrace()
        println("Correo no enviado, error: ${e.message}")
    }
}

fun crearMensajeHTML(codigo: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body {
                    background-color: #1F1C4B;
                    color: #FFFFFF;
                    font-family: Arial, sans-serif;
                    text-align: center;
                    padding: 50px;
                }
                .container {
                    background-color: #362F5C;
                    border-radius: 10px;
                    padding: 20px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                h1 {
                    color: #6959BF;
                }
                .codigo {
                    background-color: #6959BF;
                    color: #FFFFFF;
                    padding: 10px;
                    border-radius: 5px;
                    display: inline-block;
                    font-size: 24px;
                    margin-top: 20px;
                }
                .footer {
                    margin-top: 30px;
                    color: #F85D74;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Código de confirmación</h1>
                <p>¡Hola!</p>
                <p>Este es tu código de un solo uso:</p>
                <div class="codigo">$codigo</div>
                <p class="footer">Por favor, no compartas este código con nadie.</p>
            </div>
        </body>
        </html>
    """.trimIndent()
}

