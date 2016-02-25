package com.zbiljic.resterror.jackson;

import org.testng.annotations.Test;

/**
 * @author Nemanja Zbiljic
 */
public class RestErrorModuleTest {

  @Test
  public void defaultConstructorShouldBuildIndexCorrectly() {
    new RestErrorModule();
  }

}