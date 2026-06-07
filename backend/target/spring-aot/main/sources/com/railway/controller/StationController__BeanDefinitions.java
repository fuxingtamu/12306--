package com.railway.controller;

import com.railway.service.StationService;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StationController}.
 */
public class StationController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'stationController'.
   */
  private static BeanInstanceSupplier<StationController> getStationControllerInstanceSupplier() {
    return BeanInstanceSupplier.<StationController>forConstructor(StationService.class)
            .withGenerator((registeredBean, args) -> new StationController(args.get(0)));
  }

  /**
   * Get the bean definition for 'stationController'.
   */
  public static BeanDefinition getStationControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StationController.class);
    beanDefinition.setInstanceSupplier(getStationControllerInstanceSupplier());
    return beanDefinition;
  }
}
