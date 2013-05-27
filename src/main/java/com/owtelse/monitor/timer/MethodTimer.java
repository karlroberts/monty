package com.owtelse.monitor.timer;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodTimer {
	/**
	 * If set the name of the method key in the stats database will be this.
	 * If not set it will default to the Method signature that is being annotated.
	 * @return
	 */
	String value() default "";
	
	/**
	 * If set, this suffix will be added to the method key in the stats table.
	 * @return
	 */
	String suffix() default "";
	
	/**
	 * If set only run if a System property of this name is set,
	 * otherwise always run.
	 * @return
	 */
	String activeIf() default "";
	
	// TODO should I allow reflective method param values in the key? 
	//String param() default "";

}
