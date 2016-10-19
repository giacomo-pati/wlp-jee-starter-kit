package ws;

import com.google.common.collect.Sets;

import javax.ws.rs.ApplicationPath;
import java.util.Set;

@ApplicationPath("api")
public class Application extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Sets.newHashSet( //
                CalculationService.class,
                AboutService.class);
    }

}
