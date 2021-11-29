package co.com.bancolombia.edgeservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "paths.cors.urls=http://localhost:4200")
public class EdgeServiceApplicationTests {

    @Test
    @SuppressWarnings("java:S2701")
    public void contextLoads() {
        assertTrue(true);
    }
}
