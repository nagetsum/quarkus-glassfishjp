package sample.quarkus.crud.spring;

import javax.persistence.*;

@Entity
public class Employee {

    // At startup time, we already has 5 records in Employee table by import.sql.
    // So, we have to start id-sequence from "6"
    @Id
    @SequenceGenerator(name = "employeeSequence", initialValue = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeSequence")
    private Long id;
    private String name;

    public Employee() {}

    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
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
}
