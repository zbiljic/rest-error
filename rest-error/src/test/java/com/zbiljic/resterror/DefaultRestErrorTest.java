package com.zbiljic.resterror;

import org.testng.annotations.Test;

/**
 * @author Nemanja Zbiljic
 */
public class DefaultRestErrorTest {

  private final String moreInfoUrl = "https://example.org/not-found";

  @Test(expectedExceptions = NullPointerException.class)
  public void shouldThrowOnNullType() {
    new DefaultRestError(null, 404, "Not Found", "Resource could not be found", moreInfoUrl, null);
  }

}