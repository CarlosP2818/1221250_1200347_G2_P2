package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorInnerRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events.AuthorFoundReply;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events.CreateBookEvent;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorReplyListener {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "author.created.queue")
    public void receive(CreateBookEvent event) {
        System.out.println("AUTHORS: Recebi CreateBookEvent, correlationId=" + event.getCorrelationId());

        if (event.getAuthors() == null || event.getAuthors().isEmpty()) {
            System.err.println("AUTHORS: Nenhum autor no evento!");
            return;
        }

        List<AuthorInnerRequest> createdAuthors = new ArrayList<>();

        for (AuthorInnerRequest authorReq : event.getAuthors()) {
            String name = authorReq.getName() != null ? authorReq.getName() : "Autor sem nome";
            String bio = authorReq.getBio() != null ? authorReq.getBio() : "Bio não fornecida";

            CreateAuthorRequest request = new CreateAuthorRequest();
            request.setName(name);
            request.setBio(bio);
            request.setCorrelationId(event.getCorrelationId());

            var author = authorService.create(request); // cria o autor no DB

            // Criar um DTO simples para enviar de volta
            createdAuthors.add(new AuthorInnerRequest(author.getName(), author.getBio()));
        }

        // Enviar reply para o Book Service
        AuthorFoundReply reply = new AuthorFoundReply(event.getCorrelationId(), createdAuthors);
        rabbitTemplate.convertAndSend("book.replies.exchange", "author.reply", reply);
        System.out.println("AUTHORS: Reply enviado com sucesso!");
    }
}
