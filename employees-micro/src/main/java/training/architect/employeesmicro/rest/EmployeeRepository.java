package training.architect.employeesmicro.rest;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface EmployeeRepository extends EntityRepository<Employee, Long> {
}
