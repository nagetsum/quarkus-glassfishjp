package sample.quarkus.reactivepgclient;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeEmployeeResourceTestIT extends EmployeeResourceTest {
    // Execute the same tests but in native mode.
}
