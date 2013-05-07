/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.controls;

import java.lang.annotation.*;

/**
 *
 * @author patrick
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DesignableData {
    String name() default "";
    String description() default "";
    String image() default "";
	DataProcessorType[] types();
}
