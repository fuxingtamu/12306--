package com.railway.service.impl;

import com.railway.mapper.SeatTypeMapper;
import com.railway.mapper.StationMapper;
import com.railway.mapper.TrainStopMapper;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TrainServiceImpl}.
 */
public class TrainServiceImpl__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'trainServiceImpl'.
   */
  private static BeanInstanceSupplier<TrainServiceImpl> getTrainServiceImplInstanceSupplier() {
    return BeanInstanceSupplier.<TrainServiceImpl>forConstructor(StationMapper.class, TrainStopMapper.class, SeatTypeMapper.class)
            .withGenerator((registeredBean, args) -> new TrainServiceImpl(args.get(0), args.get(1), args.get(2)));
  }

  /**
   * Get the bean definition for 'trainServiceImpl'.
   */
  public static BeanDefinition getTrainServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TrainServiceImpl.class);
    InstanceSupplier<TrainServiceImpl> instanceSupplier = getTrainServiceImplInstanceSupplier();
    instanceSupplier = instanceSupplier.andThen(TrainServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
