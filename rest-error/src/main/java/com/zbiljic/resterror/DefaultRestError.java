package com.zbiljic.resterror;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
@Immutable
public class DefaultRestError implements RestError {

  private final HttpStatus status;
  private final int code;
  private final String message;
  private final String developerMessage;
  private final String moreInfoUrl;
  private final Throwable throwable;

  public DefaultRestError(final int statusCode,
                          final int code,
                          final String message,
                          final String developerMessage,
                          final String moreInfoUrl) {
    this(HttpStatus.valueOf(statusCode), code, message, developerMessage, moreInfoUrl, null);
  }

  public DefaultRestError(final HttpStatus status,
                          final int code,
                          final String message,
                          final String developerMessage,
                          final String moreInfoUrl) {
    this(status, code, message, developerMessage, moreInfoUrl, null);
  }

  public DefaultRestError(final HttpStatus status,
                          final int code,
                          final String message,
                          final String developerMessage,
                          final String moreInfoUrl,
                          @Nullable final Throwable throwable) {
    if (status == null) {
      throw new NullPointerException("HttpStatus argument cannot be null.");
    }
    this.status = status;
    this.code = code;
    this.message = message;
    this.developerMessage = developerMessage;
    this.moreInfoUrl = moreInfoUrl;
    this.throwable = throwable;
  }

  @Override
  public int getStatus() {
    return status.value();
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public String getDeveloperMessage() {
    return developerMessage;
  }

  @Override
  public String getMoreInfo() {
    return moreInfoUrl;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(status.value());
    sb.append(",");
    sb.append(code);
    sb.append("{");
    if (message != null) {
      sb.append(message);
    }
    if (developerMessage != null) {
      sb.append(", ");
      sb.append(developerMessage);
    }
    if (moreInfoUrl != null) {
      sb.append(", ");
      sb.append(moreInfoUrl);
    }
    sb.append("}");
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DefaultRestError error = (DefaultRestError) o;

    if (code != error.code) return false;
    if (status != error.status) return false;
    if (message != null ? !message.equals(error.message) : error.message != null) return false;
    if (developerMessage != null ? !developerMessage.equals(error.developerMessage) : error.developerMessage != null)
      return false;
    if (moreInfoUrl != null ? !moreInfoUrl.equals(error.moreInfoUrl) : error.moreInfoUrl != null)
      return false;
    return throwable != null ? throwable.equals(error.throwable) : error.throwable == null;
  }

  @Override
  public int hashCode() {
    int result = status.hashCode();
    result = 31 * result + code;
    result = 31 * result + (message != null ? message.hashCode() : 0);
    result = 31 * result + (developerMessage != null ? developerMessage.hashCode() : 0);
    result = 31 * result + (moreInfoUrl != null ? moreInfoUrl.hashCode() : 0);
    result = 31 * result + (throwable != null ? throwable.hashCode() : 0);
    return result;
  }
}
