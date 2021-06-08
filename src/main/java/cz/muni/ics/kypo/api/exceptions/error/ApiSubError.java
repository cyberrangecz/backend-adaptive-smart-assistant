package cz.muni.ics.kypo.api.exceptions.error;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

@ApiModel(value = "ApiSubError", subTypes = {JavaApiError.class},
        description = "Superclass for classes JavaApiError and PythonApiError")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaApiError.class, name = "JavaApiError")})
public abstract class ApiSubError {

    public abstract String getMessage();
}
