package com.railway.service.impl;

import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PassengerServiceImpl}.
 */
public class PassengerServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PassengerServiceImpl apply(RegisteredBean registeredBean,
      PassengerServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("baseMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
