package sample.quarkus.reactivepgclient;

public class Employee {
    public Employee() {}

    public Employee(String name) {
        this.name = name;
    }

    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
