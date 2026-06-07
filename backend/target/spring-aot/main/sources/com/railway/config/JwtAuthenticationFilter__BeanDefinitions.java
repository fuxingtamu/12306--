package com.railway.config;

import com.railway.utils.JwtUtils;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link JwtAuthenticationFilter}.
 */
public class JwtAuthenticationFilter__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'jwtAuthenticationFilter'.
   */
  private static BeanInstanceSupplier<JwtAuthenticationFilter> getJwtAuthenticationFilterInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<JwtAuthenticationFilter>forConstructor(JwtUtils.class)
            .withGenerator((registeredBean, args) -> new JwtAuthenticationFilter(args.get(0)));
  }

  /**
   * Get the bean definition for 'jwtAuthenticationFilter'.
   */
  public static BeanDefinition getJwtAuthenticationFilterBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(JwtAuthenticationFilter.class);
    beanDefinition.setInstanceSupplier(getJwtAuthenticationFilterInstanceSupplier());
    return beanDefinition;
  }
}
