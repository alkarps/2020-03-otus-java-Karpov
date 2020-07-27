package my.alkarps.engine.exception.validate;

import my.alkarps.engine.exception.TestClassException;

import java.lang.annotation.Annotation;

/**
 * @author alkarps
 * create date 23.07.2020 9:13
 */
public class MethodNotHasStaticModifierException extends TestClassException {
    public MethodNotHasStaticModifierException(Class<? extends Annotation> annotation) {
        super(String.format("Метод, аннотированный %s, должен быть только с модификатором static",
                annotation == null ? "null" : annotation.getSimpleName()));
    }
}
