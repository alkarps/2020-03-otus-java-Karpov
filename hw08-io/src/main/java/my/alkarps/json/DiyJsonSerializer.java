package my.alkarps.json;

import javax.json.Json;
import javax.json.JsonValue;

/**
 * @author alkarps
 * create date 28.07.2020 15:39
 */
public class DiyJsonSerializer {

    public static String toJson(Object object) {
        JsonValue value = JsonValue.NULL;
        if (object != null) {
            Class<?> objectClass = object.getClass();
            System.out.println(objectClass.getName());
            if (objectClass.isPrimitive()) {
                value = primitiveValue(objectClass, object);
            } else {
                value = Json.createObjectBuilder().build();
            }
        }
        return value.toString();
    }

    private static JsonValue primitiveValue(Class<?> objectClass, Object object) {
        JsonValue value = JsonValue.EMPTY_JSON_OBJECT;
        if (objectClass.equals(boolean.class)) {
            return booleanValue((boolean) object);
        }
        if (objectClass.equals(byte.class) || objectClass.equals(short.class) || objectClass.equals(int.class)) {
            return Json.createValue((Integer) object);
        }
        if (objectClass.equals(long.class)) {
            return Json.createValue((Long) object);
        }
        if(objectClass.equals(char.class)){
//            return Json.createValue();
        }
        return value;
    }

    private static JsonValue booleanValue(boolean value) {
        return value ? JsonValue.TRUE : JsonValue.FALSE;
    }
}
