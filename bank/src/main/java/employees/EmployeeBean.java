package employees;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Named
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmployeeBean {

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeMapper employeeMapper;
    @Inject
    private NameMapper nameMapper;

    public List<EmployeeDto> listEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::employeeToEmployeeDto)
                .collect(Collectors.toList()); }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        var employee = new Employee();
        employee.setName(nameMapper.nameDtoToName(command.getName()));
        employee.setMother(nameMapper.nameDtoToName(command.getMother()));

        var created = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(created);
    }
}
