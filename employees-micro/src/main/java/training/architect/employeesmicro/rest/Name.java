package training.architect.employeesmicro.rest;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Name {

    private String forename;

    private String surename;

}
