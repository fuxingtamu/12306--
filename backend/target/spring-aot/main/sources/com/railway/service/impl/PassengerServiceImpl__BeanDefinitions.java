package com.railway.service.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PassengerServiceImpl}.
 */
public class PassengerServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'passengerServiceImpl'.
   */
  public static BeanDefinition getPassengerServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PassengerServiceImpl.class);
    InstanceSupplier<PassengerServiceImpl> instanceSupplier = InstanceSupplier.using(PassengerServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PassengerServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
