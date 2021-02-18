package cz.muni.ics.kypo.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error when calling elasticsearch service REST API")
public class ElasticsearchServiceException extends RuntimeException {

    public ElasticsearchServiceException() {
        super();
    }

    public ElasticsearchServiceException(String message) {
        super(message);
    }

    public ElasticsearchServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticsearchServiceException(Throwable cause) {
        super(cause);
    }

    public ElasticsearchServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
