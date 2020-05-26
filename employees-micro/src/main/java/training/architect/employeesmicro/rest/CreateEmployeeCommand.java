package training.architect.employeesmicro.rest;

import lombok.Data;

import javax.persistence.Embedded;

@Data
public class CreateEmployeeCommand {

    @Embedded
    private Name nameOfEmployee;

    @Embedded
    private Name nameOfMother;
}
