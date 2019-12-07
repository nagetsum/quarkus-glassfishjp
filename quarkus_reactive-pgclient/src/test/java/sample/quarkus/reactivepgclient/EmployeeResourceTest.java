package sample.quarkus.reactivepgclient;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
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
        assertThat(employees.get(0).getId(), equalTo(1L));
        assertThat(employees.get(0).getName(), equalTo("Penny"));
        assertThat(employees.get(1).getId(), equalTo(2L));
        assertThat(employees.get(1).getName(), equalTo("Sheldon"));
        assertThat(employees.get(2).getId(), equalTo(3L));
        assertThat(employees.get(2).getName(), equalTo("Amy"));
        assertThat(employees.get(3).getId(), equalTo(4L));
        assertThat(employees.get(3).getName(), equalTo("Leonard"));
        assertThat(employees.get(4).getId(), equalTo(5L));
        assertThat(employees.get(4).getName(), equalTo("Bernadette"));
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
