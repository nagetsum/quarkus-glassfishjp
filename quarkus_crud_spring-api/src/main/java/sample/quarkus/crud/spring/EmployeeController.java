package sample.quarkus.crud.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(value = "/all", produces = "application/json")
    public Iterable<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException("employee id = " + id + " is not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestParam("name") String name) {
        Employee employee = new Employee();
        employee.setName(name);
        employeeRepository.save(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> resign(@PathVariable long id) {
        if (employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
