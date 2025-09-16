package io.github.client.module.data;

import java.lang.annotation.*;

/**
 * @author markuss
 * @since 04/05/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {
    String name();
    String description() default "";
    ModuleCategory category();
}