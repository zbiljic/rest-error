package com.zbiljic.resterror.ws.rs.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class Classes {
  /** Private internal log instance. */
  private static final Logger log = LoggerFactory.getLogger(Classes.class);

  private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
    @Override
    protected ClassLoader doGetClassLoader() throws Throwable {
      return Thread.currentThread().getContextClassLoader();
    }
  };

  private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
    @Override
    protected ClassLoader doGetClassLoader() throws Throwable {
      return Classes.class.getClassLoader();
    }
  };

  private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
    @Override
    protected ClassLoader doGetClassLoader() throws Throwable {
      return ClassLoader.getSystemClassLoader();
    }
  };

  /**
   * Returns the specified resource by checking the current thread's {@link
   * Thread#getContextClassLoader() context class loader}, then the current ClassLoader
   * (<code>Classes.class.getClassLoader()</code>), then the system/application ClassLoader
   * (<code>ClassLoader.getSystemClassLoader()</code>, in that order, using {@link
   * ClassLoader#getResourceAsStream(String) getResourceAsStream(name)}.
   *
   * @param name the name of the resource to acquire from the classloader(s).
   * @return the InputStream of the resource found, or <code>null</code> if the resource cannot be
   * found from any of the three mentioned ClassLoaders.
   */
  public static InputStream getResourceAsStream(String name) {

    InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);

    if (is == null) {
      is = CLASS_CL_ACCESSOR.getResourceStream(name);
    }

    if (is == null) {
      is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
    }

    return is;
  }

  private static interface ClassLoaderAccessor {
    <T> Class<T> loadClass(String fqcn);

    InputStream getResourceStream(String name);
  }

  private static abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

    @SuppressWarnings("unchecked")
    public <T> Class<T> loadClass(String fqcn) {
      Class<T> clazz = null;
      ClassLoader cl = getClassLoader();
      if (cl != null) {
        try {
          clazz = (Class<T>) cl.loadClass(fqcn);
        } catch (ClassNotFoundException e) {
          if (log.isTraceEnabled()) {
            log.trace("Unable to load clazz named [" + fqcn + "] from class loader [" + cl + "]");
          }
        }
      }
      return clazz;
    }

    public InputStream getResourceStream(String name) {
      InputStream is = null;
      ClassLoader cl = getClassLoader();
      if (cl != null) {
        is = cl.getResourceAsStream(name);
      }
      return is;
    }

    protected final ClassLoader getClassLoader() {
      try {
        return doGetClassLoader();
      } catch (Throwable t) {
        if (log.isDebugEnabled()) {
          log.debug("Unable to acquire ClassLoader.", t);
        }
      }
      return null;
    }

    protected abstract ClassLoader doGetClassLoader() throws Throwable;
  }
}
