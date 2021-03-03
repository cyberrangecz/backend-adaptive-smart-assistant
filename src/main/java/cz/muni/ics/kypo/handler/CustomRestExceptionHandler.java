package cz.muni.ics.kypo.handler;

import cz.muni.ics.kypo.api.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.api.exceptions.error.ApiEntityError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    /**
     * Handle entity not found exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex, final WebRequest request, HttpServletRequest req) {
        final ApiEntityError apiError = ApiEntityError.of(
                EntityNotFoundException.class.getAnnotation(ResponseStatus.class).value(),
                EntityNotFoundException.class.getAnnotation(ResponseStatus.class).reason(),
                getFullStackTrace(ex),
                URL_PATH_HELPER.getRequestUri(req),
                ex.getEntityErrorDetail());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private String getFullStackTrace(Exception exception) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            String fullStackTrace = sw.toString();
            LOG.error(fullStackTrace);
            return fullStackTrace;
        } catch (IOException e) {
            LOG.error("It was not possible to get the stack trace for that exception: ", e);
        }
        return "It was not possible to get the stack trace for that exception.";
    }
}
