package sample.quarkus.jpajaxrscdijta;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Specify test orders because
 * testRegister method add a record and does not cleanup Employee table on PostgresSQL each test method execution.
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeResourceTest {

    @Test
    @Order(1)
    public void testAll() {
        List<Employee> employees = given()
                .contentType(ContentType.JSON)
                .when().get("/all").as(new TypeRef<List<Employee>>() {});

        assertThat(employees, hasSize(5));
        assertThat(employees.get(0).id, equalTo(1L));
        assertThat(employees.get(0).name, equalTo("Penny"));
        assertThat(employees.get(1).id, equalTo(2L));
        assertThat(employees.get(1).name, equalTo("Sheldon"));
        assertThat(employees.get(2).id, equalTo(3L));
        assertThat(employees.get(2).name, equalTo("Amy"));
        assertThat(employees.get(3).id, equalTo(4L));
        assertThat(employees.get(3).name, equalTo("Leonard"));
        assertThat(employees.get(4).id, equalTo(5L));
        assertThat(employees.get(4).name, equalTo("Bernadette"));
    }

    @Test
    @Order(2)
    public void testFindById() {
        testFindById(1, "Penny");
    }

    private void testFindById(int id, String name) {
        given()
                .pathParam("id", id)
                .when().get("/{id}")
                .then()
                .statusCode(200)
                .body("id", is(id), "name", is(name));
    }

    @Test
    @Order(3)
    public void testFindByIdNotFound() {
        given()
                .pathParam("id", 99L)
                .when().get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    public void testRegister() {
        int maxId = given()
                .contentType(ContentType.JSON)
                .when().get("/all").as(new TypeRef<List<Employee>>() {}).size();

        int createdId = maxId + 1;
        given()
                .queryParam("name", "test-user")
                .when().post("/")
                .then()
                .statusCode(201);

        testFindById(createdId, "test-user");
    }

    @Test
    @Order(5)
    public void testResign() {
        given()
                .pathParam("id", 1)
                .when().delete("/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", 1)
                .when().get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(6)
    public void testResignNotEmployee() {
        given()
                .pathParam("id", 99)
                .when().delete("/{id}")
                .then()
                .statusCode(404);
    }
}
