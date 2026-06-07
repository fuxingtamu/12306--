package com.railway.controller;

import com.railway.service.TrainService;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TrainController}.
 */
public class TrainController__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'trainController'.
   */
  private static BeanInstanceSupplier<TrainController> getTrainControllerInstanceSupplier() {
    return BeanInstanceSupplier.<TrainController>forConstructor(TrainService.class)
            .withGenerator((registeredBean, args) -> new TrainController(args.get(0)));
  }

  /**
   * Get the bean definition for 'trainController'.
   */
  public static BeanDefinition getTrainControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TrainController.class);
    beanDefinition.setInstanceSupplier(getTrainControllerInstanceSupplier());
    return beanDefinition;
  }
}
