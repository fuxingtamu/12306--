package com.railway.service.impl;

import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link StationServiceImpl}.
 */
public class StationServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static StationServiceImpl apply(RegisteredBean registeredBean,
      StationServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("baseMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
