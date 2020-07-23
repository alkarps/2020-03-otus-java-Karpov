package my.alkarps.engine.exception.validate;

import java.lang.annotation.Annotation;

/**
 * @author alkarps
 * create date 23.07.2020 9:13
 */
public class MethodHasStaticModifierException extends NotValidClassException {
    public MethodHasStaticModifierException(Class<? extends Annotation> annotation) {
        super(String.format("Метод, аннотированный %s, должен быть без модификатора static",
                annotation == null ? "null" : annotation.getSimpleName()));
    }
}
