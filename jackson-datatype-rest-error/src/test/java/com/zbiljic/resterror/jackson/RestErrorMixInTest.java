package com.zbiljic.resterror.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;

import com.zbiljic.resterror.DefaultRestError;
import com.zbiljic.resterror.RestError;
import com.zbiljic.resterror.RestErrorFactory;
import com.zbiljic.resterror.http.HttpStatus;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorMixInTest {

  private final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new RestErrorModule());

  public RestErrorMixInTest() {
  }

  private URL getResource(String name) {
    return getClass().getClassLoader().getResource(name);
  }

  @Test
  public void shouldSerializeDefaultRestError() throws JsonProcessingException {
    final RestError error = RestErrorFactory.valueOf(HttpStatus.NOT_FOUND);
    final String json = mapper.writeValueAsString(error);

    with(json)
        .assertThat("$.*", hasSize(4))
        .assertThat("$.status", is(404))
        .assertThat("$.code", is(404))
        .assertThat("$.message", is("Not Found"))
        .assertThat("$.moreInfo", hasToString("http://httpstatus.es/404"));
  }

  @Test
  public void shouldSerializeCustomRestError() throws JsonProcessingException {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        .withCode(9999)
        .withMessage("Unknown error")
        .withDeveloperMessage("Unknown error has occurred")
        .withMoreInfoUrl("http://example.org/unknown")
        .build();

    final String json = mapper.writeValueAsString(error);

    with(json)
        .assertThat("$.*", hasSize(5))
        .assertThat("$.code", is(9999));
  }

  @Test
  public void shouldDeserializeDefaultRestError() throws IOException {
    final URL resource = this.getResource("not-found.json");
    final RestError raw = mapper.readValue(resource, RestError.class);

    assertThat(raw, instanceOf(DefaultRestError.class));
    final DefaultRestError error = (DefaultRestError) raw;

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Not Found"));
    assertThat(error.getMoreInfo(), equalTo("http://httpstatus.es/404"));
  }

}