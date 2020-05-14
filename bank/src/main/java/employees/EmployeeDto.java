package employees;

import lombok.Data;

@Data
public class EmployeeDto {

    private Long id;

    private Name name;
    private Name mother;
}
