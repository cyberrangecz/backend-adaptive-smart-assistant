package cz.cyberrange.platform.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

/** The type Elasticsearch service exception. */
@ResponseStatus(reason = "Error when calling elasticsearch service REST API")
public class ElasticsearchServiceException extends RuntimeException {

  /** Instantiates a new Elasticsearch service exception. */
  public ElasticsearchServiceException() {
    super();
  }

  /**
   * Instantiates a new Elasticsearch service exception.
   *
   * @param message the message
   */
  public ElasticsearchServiceException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Elasticsearch service exception.
   *
   * @param message the message
   * @param cause the cause
   */
  public ElasticsearchServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Instantiates a new Elasticsearch service exception.
   *
   * @param cause the cause
   */
  public ElasticsearchServiceException(Throwable cause) {
    super(cause);
  }

  /**
   * Instantiates a new Elasticsearch service exception.
   *
   * @param message the message
   * @param cause the cause
   * @param enableSuppression the enable suppression
   * @param writableStackTrace the writable stack trace
   */
  public ElasticsearchServiceException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
