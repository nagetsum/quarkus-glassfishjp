package sample.thorntail.jpajaxrscdijta;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries(
        @NamedQuery(name = "Employee.getAll", query = "SELECT e FROM Employee e")
)
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    public Employee() {}

    public Employee(String name) {
        this.name = name;
    }

    // At startup time, we already has 5 records in Employee table by import.sql.
    // So, we have to start id-sequence from "6"
    @Id
    @SequenceGenerator(name = "employeeSequence", initialValue = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeSequence")
    private long id;

    @Column(length = 40)
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
