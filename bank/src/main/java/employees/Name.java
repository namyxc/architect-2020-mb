package employees;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Name implements Serializable {
    private  String forename;
    private  String surename;

    public Name(String forename, String surename) {
        this.forename = forename;
        this.surename = surename;
    }

    public Name(){}
}
