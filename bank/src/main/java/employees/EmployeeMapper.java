package employees;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface EmployeeMapper {
    EmployeeDto employeeToEmployeeDto(Employee employee);
}
