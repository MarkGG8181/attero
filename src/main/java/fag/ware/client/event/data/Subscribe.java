package fag.ware.client.event.data;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    EventPriority priority() default EventPriority.NORMAL;
}