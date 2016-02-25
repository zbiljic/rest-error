package com.zbiljic.resterror;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
final class GenericRestError {

  private static final String BASE_MORE_INFO_URL = "http://httpstatus.es/";

  GenericRestError() throws Exception {
    throw new IllegalAccessException();
  }

  static RestErrorBuilder create(final HttpStatus status) {
    return new RestErrorBuilder()
        .withStatus(status)
        .withCode(status.value())
        .withMessage(status.getReasonPhrase())
        .withMoreInfoUrl(BASE_MORE_INFO_URL.concat(String.valueOf(status.value())));
  }

}
