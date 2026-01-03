package pt.psoft.g1.psoftg1.bootstrapping;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
@PropertySource({ "classpath:config/library.properties" })
@Order(2)
public class Bootstrapper implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Value("${lendingDurationInDays}")
    private int lendingDurationInDays;

    @Value("${fineValuePerDayInCents}")
    private int fineValuePerDayInCents;

    @Override
    @Transactional
    public void run(String... args) {
        createBooks();
    }

    private void createBooks() {

        // 1. O País das Pessoas de Pernas Para o Ar
        if (bookRepository.findByIsbn("9789720706386").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789720706386"),
                    new Title("O País das Pessoas de Pernas Para o Ar"),
                    new Description("Fazendo uso do humor e do nonsense, o livro reúne quatro histórias divertidas e com múltiplos significados."),
                    "1",  // Id do género (pode ser hardcoded)
                    List.of("1"), // Id dos autores (hardcoded)
                    null
            );
            bookRepository.save(book);
        }

        // 2. Como se Desenha Uma Casa
        if (bookRepository.findByIsbn("9789723716160").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789723716160"),
                    new Title("Como se Desenha Uma Casa"),
                    new Description("Como quem, vindo de países distantes, chega finalmente aonde sempre esteve."),
                    "2",
                    List.of("2"),
                    null
            );
            bookRepository.save(book);
        }

        // 3. C e Algoritmos
        if (bookRepository.findByIsbn("9789895612864").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789895612864"),
                    new Title("C e Algoritmos"),
                    new Description("O C é uma linguagem de programação incontornável e precursor de várias linguagens modernas."),
                    "3",
                    List.of("3"),
                    null
            );
            bookRepository.save(book);
        }

        // 4. Introdução ao Desenvolvimento Moderno para a Web
        if (bookRepository.findByIsbn("9782722203402").isEmpty()) {
            Book book = new Book(
                    new Isbn("9782722203402"),
                    new Title("Introdução ao Desenvolvimento Moderno para a Web"),
                    new Description("Livro focado no desenvolvimento moderno de aplicações Web, divididas em front-end e back-end."),
                    "4",
                    List.of("4", "5"),
                    null
            );
            bookRepository.save(book);
        }

        // 5. O Principezinho
        if (bookRepository.findByIsbn("9789722328296").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789722328296"),
                    new Title("O Principezinho"),
                    new Description("Depois de deixar o seu asteroide, o principezinho chega à Terra e conta todas as aventuras que viveu."),
                    "5",
                    List.of("6"),
                    "bookPhotoTest.jpg"
            );
            bookRepository.save(book);
        }

        // 6. A Criada Está a Ver
        if (bookRepository.findByIsbn("9789895702756").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789895702756"),
                    new Title("A Criada Está a Ver"),
                    new Description("MILLIE, a memorável protagonista, está de volta. História cheia de suspense e mistério."),
                    "4",
                    List.of("7"),
                    null
            );
            bookRepository.save(book);
        }

        // 7. O Hobbit
        if (bookRepository.findByIsbn("9789897776090").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789897776090"),
                    new Title("O Hobbit"),
                    new Description("Bilbo Baggins vive uma aventura inesperada com Gandalf e treze anões."),
                    "5",
                    List.of("8"),
                    null
            );
            bookRepository.save(book);
        }

        // 8. Histórias de Vigaristas e Canalhas
        if (bookRepository.findByIsbn("9789896379636").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789896379636"),
                    new Title("Histórias de Vigaristas e Canalhas"),
                    new Description("Contos sobre vigaristas e canalhas, organizados por George R. R. Martin e Gardner Dozois."),
                    "5",
                    List.of("8, 9"),
                    null
            );
            bookRepository.save(book);
        }

        // 9. Histórias de Aventureiros e Patifes
        if (bookRepository.findByIsbn("9789896378905").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789896378905"),
                    new Title("Histórias de Aventureiros e Patifes"),
                    new Description("Contos sobre patifes, mercenários e vigaristas organizados por George R. R. Martin e Gardner Dozois."),
                    "5",
                    List.of("8", "9"),
                    null
            );
            bookRepository.save(book);
        }

        // 10. Windhaven
        if (bookRepository.findByIsbn("9789896375225").isEmpty()) {
            Book book = new Book(
                    new Isbn("9789896375225"),
                    new Title("Windhaven"),
                    new Description("Novo planeta com voadores de asas metálicas, onde Maris desafia a tradição para conquistar os céus."),
                    "5",
                    List.of("8", "10"),
                    null
            );
            bookRepository.save(book);
        }
    }
}
