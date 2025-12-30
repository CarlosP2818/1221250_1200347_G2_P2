package pt.psoft.g1.psoftg1.usermanagement.model.dto;

public record RoleDto(String authority) {
    public static final String ADMIN = "ADMIN";
    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String READER = "READER";

}
