package sample.quarkus.jpajaxrscdijta;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class EmployeeService {

    private static final Logger LOG = Logger.getLogger(EmployeeService.class);

    public List<Employee> getAll() {
        return Employee.findAll().list();

        // JPQL
//        return Employee.find("SELECT e FROM Employee e").list();

        // Paging : make it use pages of 25 entries at a time
//        PanacheQuery<Employee> allEmployee = Employee.find("SELECT e FROM Employee e");
//        allEmployee.page(Page.ofSize(25));
//        return allEmployee.stream()
//                .collect(Collectors.toList());
    }

    public Optional<Employee> get(long id) {
        return Optional.ofNullable(Employee.findById(id));
    }

    public long register(String name) {
        Employee newEmployee = new Employee(name);
        Employee.persist(newEmployee);
        LOG.infof("New employee is registered : {1}", newEmployee);
        return newEmployee.id;
    }

    public void resign(long id) {
        Employee e = Employee.findById(id);
        if (e == null) {
            throw new ApplicationException("Employee id = " + id + " is not found");
        }
        e.delete();
        LOG.infof("Employee id = {1} is deleted", id);
    }
}
