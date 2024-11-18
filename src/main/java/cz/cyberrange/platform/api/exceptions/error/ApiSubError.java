package cz.cyberrange.platform.api.exceptions.error;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.annotations.ApiModel;

@ApiModel(value = "ApiSubError", subTypes = {JavaApiError.class},
        description = "Superclass for classes JavaApiError and PythonApiError")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaApiError.class, name = "JavaApiError")})
public abstract class ApiSubError {

    public abstract String getMessage();
}
