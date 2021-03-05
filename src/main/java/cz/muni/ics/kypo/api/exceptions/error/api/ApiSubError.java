package cz.muni.ics.kypo.api.exceptions.error.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

@ApiModel(value = "ApiSubError", subTypes = {JavaApiError.class},
        description = "Superclass for classes JavaApiError and PythonApiError")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JavaApiError.class, name = "JavaApiError")})
public abstract class ApiSubError {
    @ApiModelProperty(value = "The HTTP response status code", example = "404 Not found (different for each type of exception).")
    private HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
