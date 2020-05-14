package employees;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class EmployeeBean {

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeMapper employeeMapper;

    public List<EmployeeDto> listEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        var employee = employeeMapper.createEmployeeCommandToEmployee(command);

        var created = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(created);
    }
}
