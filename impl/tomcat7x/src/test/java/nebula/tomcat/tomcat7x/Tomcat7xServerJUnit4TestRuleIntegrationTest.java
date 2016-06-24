package nebula.tomcat.tomcat7x;

import nebula.tomcat.tomcat7x.junit4.Tomcat7XServerRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class Tomcat7xServerJUnit4TestRuleIntegrationTest {
    @Rule
    public TestRule rule = new Tomcat7XServerRule();

    @Test
    public void something() throws InterruptedException {
        System.out.println("Testing in progress...");
        Thread.sleep(5000);
    }
}
