package com.greensqa.datagen.email;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.nio.file.Path;
import java.util.Properties;

/**
 * Envía el archivo CSV generado por correo electrónico (Bonus 7).
 *
 * <p>Se configura por variables de entorno para no exponer credenciales en código:
 * <ul>
 *   <li>SMTP_HOST, SMTP_PORT</li>
 *   <li>SMTP_USER, SMTP_PASSWORD</li>
 *   <li>MAIL_FROM, MAIL_TO</li>
 * </ul>
 * Si no están configuradas, {@link #estaConfigurado()} devuelve false y el envío se omite.</p>
 */
public class EmailSender {

    private final String host = System.getenv("SMTP_HOST");
    private final String port = System.getenv().getOrDefault("SMTP_PORT", "587");
    private final String user = System.getenv("SMTP_USER");
    private final String password = System.getenv("SMTP_PASSWORD");
    private final String from = System.getenv().getOrDefault("MAIL_FROM",
            System.getenv("SMTP_USER"));
    private final String to = System.getenv("MAIL_TO");

    public boolean estaConfigurado() {
        return notBlank(host) && notBlank(user) && notBlank(password) && notBlank(to);
    }

    public void enviarCsv(Path csv, long cantidad) {
        if (!estaConfigurado()) {
            throw new IllegalStateException(
                    "SMTP no configurado. Defina SMTP_HOST, SMTP_USER, SMTP_PASSWORD y MAIL_TO.");
        }
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new jakarta.mail.PasswordAuthentication(user, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("[GreenSQA] Datos de prueba LATAM - " + cantidad + " registros");

            MimeBodyPart texto = new MimeBodyPart();
            texto.setText("Adjunto el archivo de datos de prueba generados ("
                    + cantidad + " registros).\n\nEquipo Automatización GreenSQA.");

            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(csv.toFile())));
            adjunto.setFileName(csv.getFileName().toString());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el correo", e);
        }
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
