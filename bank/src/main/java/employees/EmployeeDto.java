package employees;

import lombok.Data;

@Data
public class EmployeeDto {

    private Long id;

    private NameDto name;
    private NameDto mother;
}
