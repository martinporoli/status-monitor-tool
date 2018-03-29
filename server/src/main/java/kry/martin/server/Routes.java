package kry.martin.server;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.vertx.core.http.HttpMethod;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Routes {
	String path();
	HttpMethod method() default HttpMethod.GET;
}
