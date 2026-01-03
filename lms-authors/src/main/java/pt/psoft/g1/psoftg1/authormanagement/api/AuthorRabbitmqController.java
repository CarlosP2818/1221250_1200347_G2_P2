package pt.psoft.g1.psoftg1.authormanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;

import java.nio.charset.StandardCharsets;

@Service
public class AuthorRabbitmqController {

    @Autowired
    private AuthorService authorService;

    @RabbitListener(queues = "#{autoDeleteQueue_Author_Created.name}")
    public void receiveAuthorCreatedMsg(Message msg) {

        try {
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            CreateAuthorRequest createAuthorRequest = objectMapper.readValue(jsonReceived, CreateAuthorRequest.class);

            System.out.println(" [x] Received Author Created by AMQP: " + msg + ".");
            try {
                authorService.create(createAuthorRequest);
                System.out.println(" [x] New Author inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] Author already exists. No need to store it.");
            }
        }
        catch(Exception ex) {
            System.out.println(" [x] Exception receiving Author event from AMQP: '" + ex.getMessage() + "'");
        }
    }

}