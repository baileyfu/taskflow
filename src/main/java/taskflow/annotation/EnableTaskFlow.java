package taskflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import taskflow.config.TaskFlowConfiguration;

/**
 * Annotation that enables Taskflow by annotating Configuration classes.Only one occurrence of this annotation is needed.
 * @author bailey
 * @date 2019年2月19日 下午5:38:32 
 * @version 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(TaskFlowConfiguration.class)
public @interface EnableTaskFlow {
}
