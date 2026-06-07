package com.railway;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassUtils;

/**
 * Bean definitions for {@link RailwaySystemApplication}.
 */
public class RailwaySystemApplication__BeanDefinitions {
  /**
   * Get the bean definition for 'railwaySystemApplication'.
   */
  public static BeanDefinition getRailwaySystemApplicationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RailwaySystemApplication.class);
    beanDefinition.setTargetType(RailwaySystemApplication.class);
    ConfigurationClassUtils.initializeConfigurationClass(RailwaySystemApplication.class);
    beanDefinition.setInstanceSupplier(RailwaySystemApplication$$SpringCGLIB$$0::new);
    return beanDefinition;
  }
}
