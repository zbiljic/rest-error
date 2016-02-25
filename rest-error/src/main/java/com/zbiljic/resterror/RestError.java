package com.zbiljic.resterror;

/**
 * @author Nemanja Zbiljic
 */
public interface RestError {

  /**
   * The corresponding HTTP status code.
   */
  int getStatus();

  /**
   * An application-specific error code that can be used to obtain more information.
   */
  int getCode();

  /**
   * A simple, easy to understand message that you can show directly to your application’s
   * end-user.
   */
  String getMessage();

  /**
   * A clear, plain text explanation with technical details that might assist a developer calling
   * the API.
   */
  String getDeveloperMessage();

  /**
   * A fully qualified URL that may be accessed to obtain more information about the error.
   */
  String getMoreInfo();
}
