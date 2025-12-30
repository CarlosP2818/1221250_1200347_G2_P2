package pt.psoft.g1.psoftg1.readermanagement.model.Dto;

public record RoleDto(String authority) {
    public static final String ADMIN = "ADMIN";
    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String READER = "READER";

}
