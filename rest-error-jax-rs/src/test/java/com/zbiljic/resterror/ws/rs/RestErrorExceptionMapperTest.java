package com.zbiljic.resterror.ws.rs;

import org.testng.annotations.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import com.zbiljic.resterror.DefaultRestError;
import com.zbiljic.resterror.RestError;
import com.zbiljic.resterror.RestErrorFactory;
import com.zbiljic.resterror.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.testng.Assert.assertEquals;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorExceptionMapperTest {

  private final RestErrorExceptionMapper mapper = new RestErrorExceptionMapper();

  @Test
  public void testNoRegisteredExceptionMapping() throws Exception {
    final Response response = mapper.toResponse(new ReflectiveOperationException());
    assertEquals(response.getStatus(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
  }

  @Test
  public void testRegisterExceptionMapping() throws Exception {
    RestError error = RestErrorFactory.builder()
        .withStatus(HttpStatus.NOT_FOUND)
        .build();
    mapper.register(ClassNotFoundException.class, error);
  }

  @Test(dependsOnMethods = "testRegisterExceptionMapping")
  public void testToResponseNewlyRegistered() {
    final Response response = mapper.toResponse(new ClassNotFoundException());
    assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
  }

  @Test
  public void testPropertiesMapping() throws Exception {
    Response response;
    DefaultRestError error;

    // IllegalArgumentException
    response = mapper.toResponse(new IllegalArgumentException("Unknown file type"));
    assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
    error = (DefaultRestError) response.getEntity();
    assertThat(error.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
    assertThat(error.getCode(), equalTo(HttpStatus.BAD_REQUEST.value()));
    assertThat(error.getMessage(), equalTo("Unknown file type"));
    assertThat(error.getDeveloperMessage(), isEmptyOrNullString());
    assertThat(error.getMoreInfo(), equalTo("http://httpstatus.es/400"));
    assertThat(error.getThrowable(), instanceOf(IllegalArgumentException.class));

    // UnknownResourceException
    response = mapper.toResponse(new NotFoundException());
    assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    error = (DefaultRestError) response.getEntity();
    assertThat(error.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
    assertThat(error.getCode(), equalTo(HttpStatus.NOT_FOUND.value()));
    assertThat(error.getMessage(), equalTo("The specified resource does not exist."));
    assertThat(error.getDeveloperMessage(), equalTo("The specified resource does not exist."));
    assertThat(error.getMoreInfo(), equalTo("http://httpstatus.es/404"));
    assertThat(error.getThrowable(), instanceOf(NotFoundException.class));
  }

}