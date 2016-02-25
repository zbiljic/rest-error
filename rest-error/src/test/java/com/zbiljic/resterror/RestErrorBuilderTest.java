package com.zbiljic.resterror;

import org.testng.annotations.Test;

import com.zbiljic.resterror.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorBuilderTest {

  private final String moreInfoUrl = "https://example.org/not-found";

  @Test
  public void shouldCreateRestError() {
    final RestError error = RestErrorFactory.builder()
        .build();

    assertThat(error.getStatus(), equalTo(500));
    assertThat(error.getCode(), equalTo(0));
    assertThat(error.getMessage(), isEmptyOrNullString());
    assertThat(error.getDeveloperMessage(), isEmptyOrNullString());
    assertThat(error.getMoreInfo(), isEmptyOrNullString());
  }

  @Test
  public void shouldCreateRestErrorWithStatus() {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .build();

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(0));
    assertThat(error.getMessage(), isEmptyOrNullString());
    assertThat(error.getDeveloperMessage(), isEmptyOrNullString());
    assertThat(error.getMoreInfo(), isEmptyOrNullString());
  }

  @Test
  public void shouldCreateRestErrorWithCode() {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .withCode(404)
        .build();

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), isEmptyOrNullString());
    assertThat(error.getDeveloperMessage(), isEmptyOrNullString());
    assertThat(error.getMoreInfo(), isEmptyOrNullString());
  }

  @Test
  public void shouldCreateRestErrorWithMessage() {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .withCode(404)
        .withMessage("Not Found")
        .build();

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Not Found"));
    assertThat(error.getDeveloperMessage(), isEmptyOrNullString());
    assertThat(error.getMoreInfo(), isEmptyOrNullString());
  }

  @Test
  public void shouldCreateRestErrorWithDeveloperMessage() {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .withCode(404)
        .withMessage("Not Found")
        .withDeveloperMessage("Resource could not be found")
        .build();

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Not Found"));
    assertThat(error.getDeveloperMessage(), equalTo("Resource could not be found"));
    assertThat(error.getMoreInfo(), isEmptyOrNullString());
  }

  @Test
  public void shouldCreateRestErrorWithMoreInfoUrl() {
    final RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .withCode(404)
        .withMessage("Not Found")
        .withDeveloperMessage("Resource could not be found")
        .withMoreInfoUrl(moreInfoUrl)
        .build();

    assertThat(error.getStatus(), equalTo(404));
    assertThat(error.getCode(), equalTo(404));
    assertThat(error.getMessage(), equalTo("Not Found"));
    assertThat(error.getDeveloperMessage(), equalTo("Resource could not be found"));
    assertThat(error.getMoreInfo(), equalTo(moreInfoUrl));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void shouldThrowOnBadHttpStatus() {
    RestErrorFactory.builder()
        .withStatus(0)
        .build();
  }

}