package nebula.tomcat.tomcat7x;

import nebula.tomcat.tomcat7x.junit4.Tomcat7XServerRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Tomcat7XServerRunner.class)
public class Tomcat7xServerJUnit4ClassRunnerIntegrationTest {
    @Test
    public void something() throws InterruptedException {
        System.out.println("Testing in progress...");
        Thread.sleep(5000);
    }
}
