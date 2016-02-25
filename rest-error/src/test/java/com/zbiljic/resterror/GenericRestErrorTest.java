package com.zbiljic.resterror;

import org.testng.annotations.Test;

/**
 * @author Nemanja Zbiljic
 */
public class GenericRestErrorTest {

  @Test(expectedExceptions = Exception.class)
  public void shouldNotBeInstantiable() throws Exception {
    new GenericRestError();
  }

}