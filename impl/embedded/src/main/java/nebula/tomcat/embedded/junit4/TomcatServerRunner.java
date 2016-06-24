package nebula.tomcat.embedded.junit4;

import nebula.tomcat.embedded.CustomClassloaderTomatRunner;
import nebula.tomcat.embedded.FilteringClassloaderTomatRunner;
import nebula.tomcat.embedded.TomcatServer;
import nebula.tomcat.embedded.TomcatStartParameterSystemProperty;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public abstract class TomcatServerRunner extends BlockJUnit4ClassRunner {
    private final Class<? extends TomcatServer> tomcatServerClass;

    public TomcatServerRunner(Class<?> klass, Class<? extends TomcatServer> tomcatServerClass) throws InitializationError {
        super(klass);
        this.tomcatServerClass = tomcatServerClass;
    }

    @Override
    public void run(RunNotifier notifier) {
        System.setProperty(TomcatStartParameterSystemProperty.BACKGROUND.getKey(), "true");
        CustomClassloaderTomatRunner tomcatRunner = new FilteringClassloaderTomatRunner();

        try {
            tomcatRunner.start(tomcatServerClass.getName());
            super.run(notifier);
        }
        catch(Throwable e) {
            final Failure failure = new Failure(getDescription(), e);
            notifier.fireTestFailure(failure);
        }
        finally {
            System.clearProperty(TomcatStartParameterSystemProperty.BACKGROUND.getKey());
            tomcatRunner.stop();
        }
    }
}
