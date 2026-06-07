package com.railway.utils;

import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link JwtUtils}.
 */
public class JwtUtils__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static JwtUtils apply(RegisteredBean registeredBean, JwtUtils instance) {
    AutowiredFieldValueResolver.forRequiredField("secret").resolveAndSet(registeredBean, instance);
    AutowiredFieldValueResolver.forRequiredField("expiration").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
