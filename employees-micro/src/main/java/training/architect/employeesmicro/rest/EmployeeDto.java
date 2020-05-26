package training.architect.employeesmicro.rest;

import lombok.Data;

@Data
public class EmployeeDto {

    private Long id;

    private Name nameOfEmployee;

    private Name nameOfMother;
}
