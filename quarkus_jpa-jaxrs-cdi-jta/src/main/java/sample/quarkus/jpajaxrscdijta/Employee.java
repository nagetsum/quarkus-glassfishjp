package sample.quarkus.jpajaxrscdijta;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.*;

@Entity
public class Employee extends PanacheEntityBase {
    public Employee() {}

    public Employee(String name) {
        this.name = name;
    }

    // At startup time, we already has 5 records in Employee table by import.sql.
    // So, we have to start id-sequence from "6"
    @Id
    @SequenceGenerator(name = "employeeSequence", initialValue = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeSequence")
    public long id;

    @Column(length = 40)
    public String name;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
