package pt.psoft.g1.psoftg1.external.service;

import java.util.Optional;

public interface BookIsbnGateway {
    Optional<String> getIsbnByTitle(String title);
}
