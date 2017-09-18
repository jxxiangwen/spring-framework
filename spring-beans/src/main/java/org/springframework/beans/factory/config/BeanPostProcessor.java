/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Factory hook that allows for custom modification of new bean instances,
 * e.g. checking for marker interfaces or wrapping them with proxies.
 * <p>
 * <p>ApplicationContexts can autodetect BeanPostProcessor beans in their
 * bean definitions and apply them to any beans subsequently created.
 * Plain bean factories allow for programmatic registration of post-processors,
 * applying to all beans created through this factory.
 * <p>
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 *
 * @author Juergen Hoeller
 * @see InstantiationAwareBeanPostProcessor
 * @see DestructionAwareBeanPostProcessor
 * @see ConfigurableBeanFactory#addBeanPostProcessor
 * @see BeanFactoryPostProcessor
 * @since 10.10.2003
 */
// BeanPostProcessor，可以在spring容器实例化bean之后，在执行bean的初始化方法前后，
// 添加一些自己的处理逻辑。这里说的初始化方法，指的是下面两种：
// 1）bean实现了InitializingBean接口，对应的方法为afterPropertiesSet
// 2）在bean定义的时候，通过init-method设置的方法
// 注意：BeanPostProcessor是在spring容器加载了bean的定义文件并且实例化bean之后执行的。
// BeanPostProcessor的执行顺序是在BeanFactoryPostProcessor之后。
// spring中，有内置的一些BeanPostProcessor实现类，例如：
//
// org.springframework.context.annotation.CommonAnnotationBeanPostProcessor：支持@Resource注解的注入
// org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor：支持@Required注解的注入
// org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor：支持@Autowired注解的注入
// org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor：支持@PersistenceUnit和@PersistenceContext注解的注入
// org.springframework.context.support.ApplicationContextAwareProcessor：用来为bean注入ApplicationContext等容器对象
// 这些注解类的BeanPostProcessor，在spring配置文件中，可以通过这样的配置
// <context:component-scan base-package="*.*" /> ，自动进行注册。
// （spring通过ComponentScanBeanDefinitionParser类来解析该标签）
public interface BeanPostProcessor {

    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>before</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method). The bean will already be populated with property values.
     * The returned bean instance may be a wrapper around the original.
     * <p>The default implementation returns the given {@code bean} as-is.
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one;
     * if {@code null}, no subsequent BeanPostProcessors will be invoked
     * @throws org.springframework.beans.BeansException in case of errors
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Apply this BeanPostProcessor to the given new bean instance <i>after</i> any bean
     * initialization callbacks (like InitializingBean's {@code afterPropertiesSet}
     * or a custom init-method). The bean will already be populated with property values.
     * The returned bean instance may be a wrapper around the original.
     * <p>In case of a FactoryBean, this callback will be invoked for both the FactoryBean
     * instance and the objects created by the FactoryBean (as of Spring 2.0). The
     * post-processor can decide whether to apply to either the FactoryBean or created
     * objects or both through corresponding {@code bean instanceof FactoryBean} checks.
     * <p>This callback will also be invoked after a short-circuiting triggered by a
     * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
     * in contrast to all other BeanPostProcessor callbacks.
     * <p>The default implementation returns the given {@code bean} as-is.
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one;
     * if {@code null}, no subsequent BeanPostProcessors will be invoked
     * @throws org.springframework.beans.BeansException in case of errors
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
     * @see org.springframework.beans.factory.FactoryBean
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
