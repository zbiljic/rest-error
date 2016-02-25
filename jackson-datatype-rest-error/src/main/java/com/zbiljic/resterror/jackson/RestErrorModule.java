package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import com.zbiljic.resterror.DefaultRestError;
import com.zbiljic.resterror.RestError;
import com.zbiljic.resterror.http.HttpStatus;

/**
 * @author Nemanja Zbiljic
 */
public final class RestErrorModule extends SimpleModule {

  public RestErrorModule() {
    super(RestErrorModule.class.getSimpleName(), PackageVersion.VERSION);

    setMixInAnnotation(RestError.class, RestErrorMixIn.class);
    setMixInAnnotation(DefaultRestError.class, DefaultRestErrorMixIn.class);

    addSerializer(HttpStatus.class, new HttpStatusSerializer());
    addDeserializer(HttpStatus.class, new HttpStatusDeserializer());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return this == o;
  }
}
