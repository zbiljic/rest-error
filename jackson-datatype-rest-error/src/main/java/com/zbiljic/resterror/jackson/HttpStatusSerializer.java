package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
final class HttpStatusSerializer extends JsonSerializer<HttpStatus> {

  @Override
  public void serialize(HttpStatus value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
    generator.writeNumber(value.value());
  }
}
