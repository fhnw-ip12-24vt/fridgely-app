package ch.primeo.fridgely;

import org.springframework.context.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FridgelyContext implements ApplicationContextAware {

    static ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
