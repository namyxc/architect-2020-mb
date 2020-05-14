package employees;

import lombok.Data;

@Data
public class CreateEmployeeCommand {
    private NameDto name;
    private NameDto mother;
}
