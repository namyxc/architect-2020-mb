package employees;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Name {

    private String forename;

    private String surename;

}
