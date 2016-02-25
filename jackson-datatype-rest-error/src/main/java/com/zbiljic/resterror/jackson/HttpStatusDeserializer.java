package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
final class HttpStatusDeserializer extends JsonDeserializer<HttpStatus> {

  @Override
  public HttpStatus deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    try {
      return HttpStatus.valueOf(parser.getIntValue());
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
