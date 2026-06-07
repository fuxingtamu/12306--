package com.railway.service.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StationServiceImpl}.
 */
public class StationServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'stationServiceImpl'.
   */
  public static BeanDefinition getStationServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StationServiceImpl.class);
    InstanceSupplier<StationServiceImpl> instanceSupplier = InstanceSupplier.using(StationServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(StationServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
