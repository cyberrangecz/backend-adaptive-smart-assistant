package cz.cyberrange.platform.config;


import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The type Object mapper configuration.
 */
@Configuration
public class ObjectMappersConfiguration {

    /**
     * General object mapper bean.
     *
     * @return the object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return initializeBasicObjectMapperConfig();
    }

    /**
     * Object mapper bean used in restTemplate calls. Basically, it contains validator settings to validate object in deserialization phase using Bean Validations.
     *
     * @return the object mapper
     */
    @Bean("webClientObjectMapper")
    public ObjectMapper webClientObjectMapper() {
        ObjectMapper objectMapper = initializeBasicObjectMapperConfig();
        objectMapper.registerModule(getSimpleValidationModule());
        return objectMapper;
    }

    private ObjectMapper initializeBasicObjectMapperConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

    private SimpleModule getSimpleValidationModule() {
        SimpleModule validationModule = new SimpleModule();
        validationModule.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (deserializer instanceof BeanDeserializer) {
                    return new BeanValidationDeserializer((BeanDeserializer) deserializer);
                }
                return deserializer;
            }
        });
        return validationModule;
    }

}
