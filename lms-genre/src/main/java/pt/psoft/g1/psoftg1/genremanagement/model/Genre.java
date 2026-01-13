package pt.psoft.g1.psoftg1.genremanagement.model;

import lombok.Getter;

import java.io.Serializable;

public class Genre implements Serializable {

    @Getter
    String genre;

    public Genre(){}

    public Genre(String genre) {
        setGenre(genre);
    }

    private void setGenre(String genre) {
        if(genre == null)
            throw new IllegalArgumentException("Genre cannot be null");
        if(genre.isBlank())
            throw new IllegalArgumentException("Genre cannot be blank");
        int GENRE_MAX_LENGTH = 100;
        if(genre.length() > GENRE_MAX_LENGTH)
            throw new IllegalArgumentException("Genre has a maximum of 4096 characters");
        this.genre = genre;
    }

    public String toString() {
        return genre;
    }
}
