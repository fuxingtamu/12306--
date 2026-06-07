package com.railway.service.impl;

import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link OrderServiceImpl}.
 */
public class OrderServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static OrderServiceImpl apply(RegisteredBean registeredBean, OrderServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("baseMapper").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
