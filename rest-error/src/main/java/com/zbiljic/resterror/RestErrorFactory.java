package com.zbiljic.resterror;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * Factory for creating {@link RestError} instances.
 *
 * @author Nemanja Zbiljic
 */
public abstract class RestErrorFactory {

  /**
   * Returns new builder for creating a {@code RestError} instance.
   *
   * @return A builder for creating a {@code RestError} instance.
   */
  public static RestErrorBuilder builder() {
    return new RestErrorBuilder();
  }

  /**
   * Returns new builder for creating a {@code RestError} instance based on existing {@code
   * RestError} instance.
   *
   * @return A builder for creating a {@code RestError} instance based on existing {@code RestError}
   * instance.
   */
  public static RestErrorBuilder builderCopyOf(RestError error) {
    return new RestErrorBuilder()
        .withStatus(error.getStatus())
        .withCode(error.getCode())
        .withMessage(error.getMessage())
        .withDeveloperMessage(error.getDeveloperMessage())
        .withMoreInfoUrl(error.getMoreInfo());
  }

  /**
   * Creates new generic {@code RestError} for the specified HTTP status.
   *
   * @param status The desired HTTP status.
   * @return Generic RestError for specified HTTP status.
   * @see <a href="http://httpstatus.es/">http://httpstatus.es/</a>
   */
  public static RestError valueOf(final HttpStatus status) {
    return GenericRestError.create(status).build();
  }

  /**
   * Creates new generic {@code RestError} for the specified HTTP status with custom message.
   *
   * @param status  The desired HTTP status.
   * @param message The custom message for the error.
   * @return Generic RestError for specified http status and message.
   * @see <a href="http://httpstatus.es/">http://httpstatus.es/</a>
   */
  public static RestError valueOf(final HttpStatus status, final String message) {
    return GenericRestError.create(status).withMessage(message).build();
  }

}
