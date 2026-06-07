package org.mybatis.spring.mapper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MapperScannerConfigurer}.
 */
public class MapperScannerConfigurer__BeanDefinitions {
  /**
   * Get the bean definition for 'railwaySystemApplication#MapperScannerRegistrar#0'.
   */
  public static BeanDefinition getRailwaySystemApplicationMapperScannerRegistrarBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MapperScannerConfigurer.class);
    beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    beanDefinition.getPropertyValues().addPropertyValue("processPropertyPlaceHolders", true);
    beanDefinition.getPropertyValues().addPropertyValue("basePackage", "com.railway.mapper");
    beanDefinition.setInstanceSupplier(MapperScannerConfigurer::new);
    return beanDefinition;
  }
}
