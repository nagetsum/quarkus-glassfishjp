package sample.thorntail.jpajaxrscdijta;

import org.jboss.logging.Logger;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class EmployeeService {

    private static final Logger LOG = Logger.getLogger(EmployeeService.class);

    @PersistenceContext
    private EntityManager em;

    public List<Employee> getAll() {
        return em.createNamedQuery("Employee.getAll", Employee.class).getResultList();
    }

    public Optional<Employee> get(long id) {
        return Optional.ofNullable(em.find(Employee.class, id));
    }

    public long register(String name) {
        Employee newEmployee = new Employee(name);
        em.persist(newEmployee);
        LOG.infov("New employee is registered : {0}", newEmployee);
        return newEmployee.getId();
    }

    public void resign(long id) {
        Employee e = em.find(Employee.class, id);
        if (e == null) {
            throw new ApplicationException("Employee id = " + id + " is not found");
        }
        em.remove(e);
        LOG.infov("Employee id = {0} is deleted", id);
    }
}
