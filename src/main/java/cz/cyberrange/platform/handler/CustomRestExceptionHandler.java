package cz.cyberrange.platform.handler;

import cz.cyberrange.platform.api.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.api.exceptions.error.ApiEntityError;
import cz.cyberrange.platform.api.exceptions.error.ApiError;
import cz.cyberrange.platform.api.exceptions.error.ApiMicroserviceError;
import cz.cyberrange.platform.exceptions.BadRequestException;
import cz.cyberrange.platform.exceptions.ForbiddenException;
import cz.cyberrange.platform.exceptions.InternalServerErrorException;
import cz.cyberrange.platform.exceptions.MicroserviceApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status,
                                                        final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers,
                                                                     final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers,
                                                                          final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status,
                                                                   final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_FOUND,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
                                                                         final HttpStatus status, final WebRequest request) {
        final StringBuilder supportedHttpMethods = new StringBuilder();
        supportedHttpMethods.append(ex.getMethod());
        supportedHttpMethods.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> supportedHttpMethods.append(t + " "));

        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_FOUND,
                getInitialException(ex).getLocalizedMessage(),
                supportedHttpMethods.toString(),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers,
                                                                     final HttpStatus status, final WebRequest request) {
        final StringBuilder supportedMediaTypes = new StringBuilder();
        supportedMediaTypes.append(ex.getContentType());
        supportedMediaTypes.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> supportedMediaTypes.append(t + " "));

        final ApiError apiError = ApiError.of(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                getInitialException(ex).getLocalizedMessage(),
                supportedMediaTypes.toString(),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = ex.getBindingResult().getGlobalErrors();
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                (fieldErrors.isEmpty() ? objectErrors : fieldErrors)
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(java.util.stream.Collectors.joining(", ")),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                ex.getMostSpecificCause().getMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // Handling of own exceptions

    /**
     * Handle constraint violation response entity.
     *
     * @param ex  the ex
     * @param req the req
     * @return the response entity
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle bad request exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                BadRequestException.class.getAnnotation(ResponseStatus.class).value(),
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle forbidden exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(final ForbiddenException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                ForbiddenException.class.getAnnotation(ResponseStatus.class).value(),
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle internal server error exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(final InternalServerErrorException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                InternalServerErrorException.class.getAnnotation(ResponseStatus.class).value(),
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

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
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req),
                ex.getEntityErrorDetail());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle spring access denied exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleSpringAccessDeniedException(AccessDeniedException ex, WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.FORBIDDEN,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle illegal argument exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
// thrown from SERVICE layer (nullpointers, illegal argument etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_ACCEPTABLE,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle null pointer exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(final NullPointerException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle user and group api exception response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler({MicroserviceApiException.class})
    public ResponseEntity<Object> handleMicroserviceApiException(final MicroserviceApiException ex, final WebRequest request,
                                                                 HttpServletRequest req) {
        final ApiError apiError = ApiMicroserviceError.of(
                ex.getStatusCode(),
                ex.getMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req),
                ex.getApiSubError());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handle all response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @param req     the req
     * @return the response entity
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    private Exception getInitialException(Exception exception) {
        while (exception.getCause() != null) {
            exception = (Exception) exception.getCause();
        }
        return exception;
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

    private String getErrorMessage(Exception exception) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            LOG.error(sw.toString());
            return exception.getMessage();
        } catch (IOException ex) {
            LOG.error("It was not possible to get the stack trace for that exception: ", ex);
        }
        return "It was not possible to get the stack trace for that exception.";
    }

}
