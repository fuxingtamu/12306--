package com.railway.service.impl;

import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link TrainServiceImpl}.
 */
public class TrainServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static TrainServiceImpl apply(RegisteredBean registeredBean, TrainServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("baseMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
