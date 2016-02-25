package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.zbiljic.resterror.http.HttpStatus;

import static com.zbiljic.resterror.RestErrorConstants.CODE;
import static com.zbiljic.resterror.RestErrorConstants.DEVELOPER_MESSAGE;
import static com.zbiljic.resterror.RestErrorConstants.MESSAGE;
import static com.zbiljic.resterror.RestErrorConstants.MORE_INFO;
import static com.zbiljic.resterror.RestErrorConstants.STATUS;

/**
 * @author Nemanja Zbiljic
 */
abstract class DefaultRestErrorMixIn {

  @JsonCreator
  DefaultRestErrorMixIn(
      @JsonProperty(value = STATUS, required = true) final HttpStatus status,
      @JsonProperty(value = CODE, required = true) final int code,
      @JsonProperty(MESSAGE) final String message,
      @JsonProperty(DEVELOPER_MESSAGE) final String developerMessage,
      @JsonProperty(MORE_INFO) final String moreInfoUrl) {
  }

}
