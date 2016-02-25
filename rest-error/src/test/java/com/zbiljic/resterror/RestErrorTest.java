package com.zbiljic.resterror;

import org.testng.annotations.Test;

import com.zbiljic.resterror.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorTest {

  @Test
  public void shouldRenderEmptyRestError() {
    final RestError error = RestErrorFactory.valueOf(HttpStatus.NOT_FOUND);
    assertThat(error.toString(), hasToString("404,404{Not Found, http://httpstatus.es/404}"));
  }

  @Test
  public void shouldRenderMessage() {
    final RestError error = RestErrorFactory.valueOf(HttpStatus.NOT_FOUND, "Resource could not be found");
    assertThat(error.toString(), hasToString("404,404{Resource could not be found, http://httpstatus.es/404}"));
  }

}