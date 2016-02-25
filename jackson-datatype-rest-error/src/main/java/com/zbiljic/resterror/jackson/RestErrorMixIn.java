package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.zbiljic.resterror.DefaultRestError;
import com.zbiljic.resterror.RestError;

import static com.zbiljic.resterror.RestErrorConstants.CODE;
import static com.zbiljic.resterror.RestErrorConstants.DEVELOPER_MESSAGE;
import static com.zbiljic.resterror.RestErrorConstants.MESSAGE;
import static com.zbiljic.resterror.RestErrorConstants.MORE_INFO;
import static com.zbiljic.resterror.RestErrorConstants.STATUS;

/**
 * @author Nemanja Zbiljic
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    defaultImpl = DefaultRestError.class,
    visible = true)
@JsonInclude(Include.NON_NULL)
interface RestErrorMixIn extends RestError {

  @JsonProperty(STATUS)
  @Override
  int getStatus();

  @JsonProperty(CODE)
  @Override
  int getCode();

  @JsonProperty(MESSAGE)
  @Override
  String getMessage();

  @JsonProperty(DEVELOPER_MESSAGE)
  @Override
  String getDeveloperMessage();

  @JsonProperty(MORE_INFO)
  @Override
  String getMoreInfo();

}
