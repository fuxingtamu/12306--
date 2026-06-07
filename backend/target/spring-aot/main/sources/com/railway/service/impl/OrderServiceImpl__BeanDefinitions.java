package com.railway.service.impl;

import com.railway.mapper.OrderItemMapper;
import com.railway.mapper.PassengerMapper;
import com.railway.mapper.TrainMapper;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link OrderServiceImpl}.
 */
public class OrderServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'orderServiceImpl'.
   */
  private static BeanInstanceSupplier<OrderServiceImpl> getOrderServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<OrderServiceImpl>forConstructor(OrderItemMapper.class, PassengerMapper.class, TrainMapper.class)
            .withGenerator((registeredBean, args) -> new OrderServiceImpl(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'orderServiceImpl'.
   */
  public static BeanDefinition getOrderServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(OrderServiceImpl.class);
    InstanceSupplier<OrderServiceImpl> instanceSupplier = getOrderServiceImplInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(OrderServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
