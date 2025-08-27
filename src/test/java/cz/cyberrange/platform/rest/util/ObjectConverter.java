package cz.cyberrange.platform.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectConverter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static <T> T convertJsonToObject(String serializedObject, Class<T> objectClass)
      throws JsonProcessingException {
    return OBJECT_MAPPER.readValue(serializedObject, objectClass);
  }

  public static String convertObjectToJson(Object object) throws JsonProcessingException {
    return OBJECT_MAPPER.writeValueAsString(object);
  }
}
