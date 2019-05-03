package unipos.signature.components.dep;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by domin on 23.01.2017.
 */
@RestController
@RequestMapping("/dep")
public class DepController {

    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    DepService depService;

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportDep(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No store found for the given AuthToken and DeviceToken combination");

        LocalDateTime now = LocalDateTime.now();

        OutputStream os = response.getOutputStream();
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=\"Datenerfassungsprotokoll" + now.toString() +".json\"");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.writeValue(os, depService.getExportEntriesForStoreGuid(store.getGuid()));
        os.flush();
    }

    @RequestMapping(value = "/exportAndSend", method = RequestMethod.GET)
    public void exportDepAndSend(HttpServletRequest request, HttpServletResponse response, @RequestParam String storeGuid) throws IOException {
        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);

        LocalDateTime now = LocalDateTime.now();

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.writeValue(os, depService.getExportEntriesForStoreGuid(store.getGuid()));
        os.flush();

        // ***
        // SEND EMAIL
        // ***

        // Recipient's email ID needs to be mentioned.
        String to = store.getDepEmail();
        if(to == null)
            return;

        // Sender's email ID needs to be mentioned
        String from = "sender@gmail.com";

        final String username = "sender";//change accordingly
        final String password = "secret";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Registrierkasse: Datenerfassungsprotokoll von " + now.toLocalDate().toString());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("Angehängt befindet sich das Datenerfassungsprotokoll von " + now.toLocalDate().toString() + ". Speichern Sie die Datei sicher ab und bewahren Sie sie auf.");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(os.toByteArray(), "application/text");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("Datenerfassungsprotokoll" + now.toLocalDate().toString() +".json");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
