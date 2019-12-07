package sample.quarkus.reactivepgclient;

import io.vertx.axle.pgclient.PgPool;
import io.vertx.axle.sqlclient.Row;
import io.vertx.axle.sqlclient.RowSet;
import io.vertx.axle.sqlclient.Tuple;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EmployeeRepository {

    @Inject
    PgPool client;

    public CompletionStage<List<Employee>> getAll() {
        return client
                .preparedQuery("SELECT id, name FROM employee ORDER BY id ASC")
                .thenApply(pgRowSet -> {
                    List<Employee> list = new ArrayList<>(pgRowSet.size());
                    pgRowSet.forEach(row -> list.add(from(row)));
                    return list;
                });
    }

    public CompletionStage<Employee> get(long id) {
        return client
                .preparedQuery("SELECT id, name FROM employee WHERE id = $1", Tuple.of(id))
                .thenApply(RowSet::iterator)
                .thenApply(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public CompletionStage<Long> register(String name) {
        return client
                .preparedQuery("INSERT INTO employee (name) VALUES ($1) RETURNING (id)", Tuple.of(name))
                .thenApply(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public CompletionStage<Boolean> resign(long id) {
        return client.preparedQuery("DELETE FROM employee where id = $1", Tuple.of(id))
                .thenApply(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private static Employee from(Row row) {
        return new Employee(row.getLong("id"), row.getString("name"));
    }

    @PostConstruct
    public void prefill() {
        // block until the following queries are completed
        client.query("DROP TABLE IF EXISTS employee")
                .thenCompose(rows -> client.query("CREATE TABLE employee (id SERIAL PRIMARY KEY, name TEXT NOT NULL)"))
                .thenCompose(rows -> client.query("INSERT INTO employee (name) VALUES ('Penny')"))
                .thenCompose(rows -> client.query("INSERT INTO employee (name) VALUES ('Sheldon')"))
                .thenCompose(rows -> client.query("INSERT INTO employee (name) VALUES ('Amy')"))
                .thenCompose(rows -> client.query("INSERT INTO employee (name) VALUES ('Leonard')"))
                .thenCompose(rows -> client.query("INSERT INTO employee (name) VALUES ('Bernadette')"))
                .toCompletableFuture().join();
    }
}
