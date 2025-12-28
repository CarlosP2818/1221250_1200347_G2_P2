package pt.psoft.g1.psoftg1.readermanagement.model;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class EmailAddress implements Serializable {

    String address;

    protected EmailAddress() {}
}
