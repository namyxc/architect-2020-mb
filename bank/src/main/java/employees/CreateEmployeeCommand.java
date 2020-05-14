package employees;

import lombok.Data;

@Data
public class CreateEmployeeCommand {
    private Name name;
    private Name mother;
}
