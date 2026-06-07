package com.railway.service.impl;

import com.railway.utils.JwtUtils;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Bean definitions for {@link UserServiceImpl}.
 */
public class UserServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'userServiceImpl'.
   */
  private static BeanInstanceSupplier<UserServiceImpl> getUserServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<UserServiceImpl>forConstructor(PasswordEncoder.class, JwtUtils.class)
            .withGenerator((registeredBean, args) -> new UserServiceImpl(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'userServiceImpl'.
   */
  public static BeanDefinition getUserServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UserServiceImpl.class);
    InstanceSupplier<UserServiceImpl> instanceSupplier = getUserServiceImplInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(UserServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
