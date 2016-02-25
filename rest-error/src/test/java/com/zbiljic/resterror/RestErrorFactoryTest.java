package com.zbiljic.resterror;

import org.testng.annotations.Test;

import com.zbiljic.resterror.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorFactoryTest {

  @Test
  public void shouldCreateGenericRestError() {
    final RestError error = RestErrorFactory.valueOf(HttpStatus.NOT_FOUND);

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Not Found"));
    assertThat(error.getMoreInfo(), equalTo("http://httpstatus.es/404"));
  }

  @Test
  public void shouldCreateGenericRestErrorWithDetail() {
    final RestError error = RestErrorFactory.valueOf(HttpStatus.NOT_FOUND, "Resource could not be found");

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Resource could not be found"));
    assertThat(error.getMoreInfo(), equalTo("http://httpstatus.es/404"));
  }

}