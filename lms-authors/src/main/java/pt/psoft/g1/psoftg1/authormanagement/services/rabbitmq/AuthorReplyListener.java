package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mongo.MongoDataOutboxEventRepo;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
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
    @Autowired
    private MongoDataOutboxEventRepo mongoDataOutboxEventRepo;

    @RabbitListener(queues = "author.created.queue")
    public void receive(CreateBookEvent event) {
        System.out.println("AUTHORS: Recebi CreateBookEvent, correlationId=" + event.getCorrelationId());

        if (event.getAuthors() == null || event.getAuthors().isEmpty()) {
            System.err.println("AUTHORS: Nenhum autor no evento!");
            return;
        }

        List<AuthorInnerRequest> createdAuthors = new ArrayList<>();
        List<OutboxEventMongo> savedTempAuthors = new ArrayList<>();

        for (AuthorInnerRequest authorReq : event.getAuthors()) {
            String name = authorReq.getName() != null ? authorReq.getName() : "Autor sem nome";
            String bio = authorReq.getBio() != null ? authorReq.getBio() : "Bio não fornecida";

            // Criar autor temporário
            OutboxEventMongo tempAuthor = new OutboxEventMongo();
            tempAuthor.setName(name);
            tempAuthor.setBio(bio);
            tempAuthor.setCorrelationId(event.getCorrelationId());
            tempAuthor.setProcessed(false);

            mongoDataOutboxEventRepo.save(tempAuthor);
            savedTempAuthors.add(tempAuthor);

            // Criar autor definitivo
            CreateAuthorRequest createAuthorRequest = new CreateAuthorRequest();
            createAuthorRequest.setName(tempAuthor.getName());
            createAuthorRequest.setBio(tempAuthor.getBio());

            try {
                authorService.create(createAuthorRequest);
                System.out.println("AUTHORS: Autor criado com sucesso - " + tempAuthor.getName());
            } catch (Exception e) {
                System.err.println("AUTHORS: Erro ao criar autor - " + e.getMessage());
            }

            // Criar um DTO simples para enviar de volta
            createdAuthors.add(new AuthorInnerRequest(tempAuthor.getName(), tempAuthor.getBio()));
        }

        // Enviar reply para o Book Service
        AuthorFoundReply reply = new AuthorFoundReply(event.getCorrelationId(), createdAuthors);
        rabbitTemplate.convertAndSend(
                "book.replies.exchange",
                "author.book.reply",
                reply
        );

        System.out.println("AUTHORS: Reply enviado com sucesso!");

        finalizeTempAuthors(savedTempAuthors);
    }

    private void finalizeTempAuthors(List<OutboxEventMongo> authors) {
        for (OutboxEventMongo author : authors) {
            author.setProcessed(true);
        }
        mongoDataOutboxEventRepo.saveAll(authors);
        System.out.println("AUTHORS: Temporários finalizados com sucesso!");
    }
}
