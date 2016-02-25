package com.zbiljic.resterror.ws.rs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.zbiljic.resterror.RestError;
import com.zbiljic.resterror.RestErrorBuilder;
import com.zbiljic.resterror.RestErrorFactory;
import com.zbiljic.resterror.ws.rs.lang.Classes;
import com.zbiljic.resterror.ws.rs.lang.OrderPreservingProperties;
import com.zbiljic.resterror.ws.rs.lang.Strings;

/**
 * @author Nemanja Zbiljic
 */
@Provider
public class RestErrorExceptionMapper implements ExceptionMapper<Throwable> {
  private static final Logger log = LoggerFactory.getLogger(RestErrorExceptionMapper.class);

  public static final String DEFAULT_CONFIGURATION_FILE = "restErrors.properties";

  /** The default name of the exception attribute: "exception". */
  public static final String DEFAULT_EXCEPTION_MESSAGE_VALUE = "_exmsg";
  public static final String EXCEPTION_CONFIG_DELIMITER = "|";

  private static final String REST_ERROR_PROPERTY = "restError";
  private static final String BASE_MORE_INFO_URL_PROPERTY = REST_ERROR_PROPERTY + ".baseMoreInfoUrl";

  private Map<String, RestError> exceptionMappings = Collections.emptyMap();

  public RestErrorExceptionMapper() {
    this(DEFAULT_CONFIGURATION_FILE);
  }

  public RestErrorExceptionMapper(String configurationFile) {
    this(Classes.getResourceAsStream(configurationFile));
  }

  public RestErrorExceptionMapper(InputStream configurationStream) {
    OrderPreservingProperties props = new OrderPreservingProperties();
    props.load(configurationStream);
    this.exceptionMappings = toRestErrors(props);
  }

  public <T extends Throwable> void register(Class<T> clazz, RestError restError) {
    this.register(clazz.getName(), restError);
  }

  public void register(String className, RestError restError) {
    synchronized (this) {
      if (className == null) {
        throw new IllegalArgumentException("Class name cannot be null");
      }
      Map<String, RestError> map = new LinkedHashMap<>(exceptionMappings);
      map.put(className, restError);
      this.exceptionMappings = Collections.unmodifiableMap(map);
    }
  }

  @Override
  public Response toResponse(Throwable t) {
    RestError error = getRestError(t);
    if (error == null) {
      return Response
          .status(Response.Status.INTERNAL_SERVER_ERROR)
          .build();
    }
    return Response
        .status(error.getStatus())
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(error)
        .build();
  }

  private RestError getRestError(Throwable t) {

    RestError template = getRestErrorTemplate(t);
    if (template == null) {
      return null;
    }

    RestErrorBuilder builder = RestErrorFactory.builder()
        .withStatus(template.getStatus())
        .withCode(template.getCode())
        .withMoreInfoUrl(template.getMoreInfo())
        .withThrowable(t);

    String msg = getMessage(template.getMessage(), t);
    if (msg != null) {
      builder.withMessage(msg);
    }
    msg = getMessage(template.getDeveloperMessage(), t);
    if (msg != null) {
      builder.withDeveloperMessage(msg);
    }

    return builder.build();
  }

  private RestError getRestErrorTemplate(Throwable t) {
    if (this.exceptionMappings.isEmpty()) {
      return null;
    }
    RestError template = null;
    String dominantMapping = t.getClass().getName();
    // try exact match first
    if (this.exceptionMappings.containsKey(dominantMapping)) {
      template = this.exceptionMappings.get(dominantMapping);
    } else {
      // find most likely error
      dominantMapping = null;
      int deepest = Integer.MAX_VALUE;
      for (Map.Entry<String, RestError> entry : this.exceptionMappings.entrySet()) {
        String key = entry.getKey();
        int depth = getDepth(key, t);
        if (depth >= 0 && depth < deepest) {
          deepest = depth;
          dominantMapping = key;
          template = entry.getValue();
        }
      }
    }
    if (template != null && log.isDebugEnabled()) {
      log.debug("Resolving to RestError template '{}' for exception of type [{}], based on exception mapping [{}]",
          template, t.getClass().getName(), dominantMapping);
    }
    return template;
  }

  /**
   * Return the depth to the superclass matching.
   *
   * 0 means ex matches exactly. Returns -1 if there's no match. Otherwise, returns depth. Lowest
   * depth wins.
   */
  protected int getDepth(String exceptionMapping, Throwable t) {
    return getDepth(exceptionMapping, t.getClass(), 0);
  }

  private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
    if (exceptionClass.getName().contains(exceptionMapping)) {
      // Found it!
      return depth;
    }
    // If we've gone as far as we can go and haven't found it...
    if (exceptionClass.equals(Throwable.class)) {
      return -1;
    }
    return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
  }

  /**
   * Returns the response status message to return to the client, or {@code null} if no status
   * message should be returned.
   *
   * @return the response status message to return to the client, or {@code null} if no status
   * message should be returned.
   */
  protected String getMessage(String msg, Throwable t) {
    if (msg != null) {
      if (msg.equalsIgnoreCase(DEFAULT_EXCEPTION_MESSAGE_VALUE)) {
        msg = t.getMessage();
      }
    }
    return msg;
  }

  private static Map<String, RestError> toRestErrors(Map<String, String> propertiesMap) {
    if (propertiesMap == null || propertiesMap.isEmpty()) {
      return Collections.emptyMap();
    }

    final String baseMoreInfoUrl = getAndRemoveProperty(propertiesMap, BASE_MORE_INFO_URL_PROPERTY);

    Map<String, RestError> map = new LinkedHashMap<>(propertiesMap.size());

    for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      RestError template = postProcessRestErrorTemplate(toRestError(value), baseMoreInfoUrl);
      map.put(key, template);
    }

    return Collections.unmodifiableMap(map);
  }

  private static RestError postProcessRestErrorTemplate(RestError error, String baseMoreInfoUrl) {
    RestErrorBuilder builder = RestErrorFactory.builderCopyOf(error);

    if (error.getCode() <= 0) {
      builder.withCode(error.getStatus());
    }
    if (error.getMessage() != null) {
      if (error.getMessage().equalsIgnoreCase("null")
          || error.getMessage().equalsIgnoreCase("off")) {
        builder.withMessage(null);
      }
    }
    if (error.getDeveloperMessage() != null) {
      if (error.getDeveloperMessage().equalsIgnoreCase("null")
          || error.getDeveloperMessage().equalsIgnoreCase("off")) {
        builder.withDeveloperMessage(null);
      }
    }
    if (error.getMoreInfo() == null && baseMoreInfoUrl != null) {
      if (error.getCode() <= 0) {
        builder.withMoreInfoUrl(String.format("%s%d", baseMoreInfoUrl, error.getStatus()));
      } else {
        builder.withMoreInfoUrl(String.format("%s%d", baseMoreInfoUrl, error.getCode()));
      }
    }

    return builder.build();
  }

  private static RestError toRestError(String exceptionConfig) {
    String[] values = Strings.delimitedListToStringArray(exceptionConfig, EXCEPTION_CONFIG_DELIMITER);
    if (values == null || values.length == 0) {
      throw new IllegalStateException("Invalid config mapping. Exception names must map to a string configuration.");
    }
    if (values.length > 5) {
      throw new IllegalStateException("Invalid config mapping. Mapped values must not contain more than 2 values (code=w, msg=x, devMsg=y, moreInfoUrl=z)");
    }

    RestErrorBuilder builder = RestErrorFactory.builder();

    boolean statusSet = false;
    boolean codeSet = false;
    boolean msgSet = false;
    boolean devMsgSet = false;
    boolean moreInfoUrlSet = false;

    for (String value : values) {

      String trimmedVal = value.trim();

      //check to see if the value is an explicitly named key/value pair:
      String[] pair = Strings.split(trimmedVal, "=");
      if (pair != null) {
        //explicit attribute set:
        String pairKey = Strings.trimWhitespace(pair[0]);
        if (!Strings.hasText(pairKey)) {
          pairKey = null;
        }
        String pairValue = Strings.trimWhitespace(pair[1]);
        if (!Strings.hasText(pairValue)) {
          pairValue = null;
        }
        if ("status".equalsIgnoreCase(pairKey)) {
          int statusCode = getRequiredInt(pairKey, pairValue);
          builder.withStatus(statusCode);
          statusSet = true;
        } else if ("code".equalsIgnoreCase(pairKey)) {
          int code = getRequiredInt(pairKey, pairValue);
          builder.withCode(code);
          codeSet = true;
        } else if ("msg".equalsIgnoreCase(pairKey)) {
          builder.withMessage(pairValue);
          msgSet = true;
        } else if ("devMsg".equalsIgnoreCase(pairKey)) {
          builder.withDeveloperMessage(pairValue);
          devMsgSet = true;
        } else if ("moreInfoUrl".equalsIgnoreCase(pairKey)) {
          builder.withMoreInfoUrl(pairValue);
          moreInfoUrlSet = true;
        }
      } else {
        //not a key/value pair - use heuristics to determine what value is being set:
        int val;
        if (!statusSet) {
          val = getInt("status", trimmedVal);
          if (val > 0) {
            builder.withStatus(val);
            statusSet = true;
            continue;
          }
        }
        if (!codeSet) {
          val = getInt("code", trimmedVal);
          if (val > 0) {
            builder.withCode(val);
            codeSet = true;
            continue;
          }
        }
        if (!msgSet) {
          builder.withMessage(trimmedVal);
          msgSet = true;
          continue;
        }
        if (!devMsgSet) {
          builder.withDeveloperMessage(trimmedVal);
          devMsgSet = true;
          continue;
        }
        if (!moreInfoUrlSet) {
          builder.withMoreInfoUrl(trimmedVal);
          moreInfoUrlSet = true;
          //noinspection UnnecessaryContinue
          continue;
        }
      }
    }

    return builder.build();
  }

  private static String getAndRemoveProperty(Map<String, String> propertiesMap, String key) {
    String value = propertiesMap.get(key);
    if (value != null) {
      propertiesMap.remove(key);
    }
    return value;
  }

  private static int getRequiredInt(String key, String value) {
    try {
      int anInt = Integer.parseInt(value);
      return Math.max(-1, anInt);
    } catch (NumberFormatException e) {
      String msg = String.format("Configuration element '%s' requires an integer value.  The value specified: %s", key, value);
      throw new IllegalArgumentException(msg, e);
    }
  }

  private static int getInt(String key, String value) {
    try {
      return getRequiredInt(key, value);
    } catch (IllegalArgumentException iae) {
      return 0;
    }
  }

}
