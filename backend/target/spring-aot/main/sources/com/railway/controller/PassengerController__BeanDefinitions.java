package com.railway.controller;

import com.railway.service.PassengerService;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PassengerController}.
 */
public class PassengerController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'passengerController'.
   */
  private static BeanInstanceSupplier<PassengerController> getPassengerControllerInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<PassengerController>forConstructor(PassengerService.class)
            .withGenerator((registeredBean, args) -> new PassengerController(args.get(0)));
  }

  /**
   * Get the bean definition for 'passengerController'.
   */
  public static BeanDefinition getPassengerControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PassengerController.class);
    beanDefinition.setInstanceSupplier(getPassengerControllerInstanceSupplier());
    return beanDefinition;
  }
}
