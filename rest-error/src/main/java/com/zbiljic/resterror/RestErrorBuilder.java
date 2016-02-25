package com.zbiljic.resterror;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorBuilder {

  private HttpStatus status;
  private int code;
  private String message;
  private String developerMessage;
  private String moreInfoUrl;
  private Throwable throwable;

  RestErrorBuilder() {
  }

  public RestErrorBuilder withStatus(int statusCode) {
    this.status = HttpStatus.valueOf(statusCode);
    return this;
  }

  public RestErrorBuilder withStatus(HttpStatus status) {
    this.status = status;
    return this;
  }

  public RestErrorBuilder withCode(int code) {
    this.code = code;
    return this;
  }

  public RestErrorBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public RestErrorBuilder withDeveloperMessage(String developerMessage) {
    this.developerMessage = developerMessage;
    return this;
  }

  public RestErrorBuilder withMoreInfoUrl(String moreInfoUrl) {
    this.moreInfoUrl = moreInfoUrl;
    return this;
  }

  public RestErrorBuilder withThrowable(Throwable throwable) {
    this.throwable = throwable;
    return this;
  }

  public RestError build() {
    if (this.status == null) {
      this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return new DefaultRestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl, this.throwable);
  }

}
