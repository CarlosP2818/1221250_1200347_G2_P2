package pt.psoft.g1.psoftg1.bookmanagement.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Title implements Serializable {

    private static final int TITLE_MAX_LENGTH = 128;

    String title;

    protected Title() {
    }

    public Title(String title) {
        setTitle(title);
    }

    public void setTitle(String title) {

        /*
         * if (!StringUtilsCustom.startsOrEndsInWhiteSpace(title)) { throw new
         * IllegalArgumentException("Invalid title: " + title); }
         */
        if (title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if (title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        if (title.length() > TITLE_MAX_LENGTH)
            throw new IllegalArgumentException("Title has a maximum of " + TITLE_MAX_LENGTH + " characters");
        this.title = title.strip();
    }

    public String toString() {
        return this.title;
    }
}
