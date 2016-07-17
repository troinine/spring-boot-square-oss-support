/*
 * Copyright 2016 the original author or authors.
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
package org.springframework.boot.retrofit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.autoconfigure.retrofit.RetrofitProperties;
import org.springframework.boot.retrofit.annotation.RetrofitService;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.Assert;
import retrofit2.Retrofit;

/**
 * Instantiation aware bean post processor adapter to instantiate the bean interfaces marked with {@link RetrofitService}
 * annotation.
 * <p>
 * The beans can't be annotated in the bean definition phase as the {@link Retrofit} bean is needed in order
 * to construct the actual service instances. In addition, the service specific configurations are accessed
 * through {@link RetrofitProperties} {@link org.springframework.boot.context.properties.ConfigurationProperties}
 *
 * @author troinine
 */
public class RetrofitServiceBeanPostProcessorAdapter extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware, PriorityOrdered {
    /**
     * The name of this bean.
     */
    public static final String BEAN_NAME = "retrofitServiceBeanPostProcessorAdapter";

    private BeanFactory beanFactory;
    private RetrofitServiceFactory retrofitServiceFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        Object ret = null;

        if (beanClass.isAnnotationPresent(RetrofitService.class)) {
            ret = getRetrofitServiceFactory().createServiceInstance(beanClass, beanName);
        }

        return ret;
    }

    /**
     * Lazy-inits the associated Retrofit service factory because the needed dependencies are available after
     * the needed bean dependencies have been created by the {@link BeanFactory}.
     *
     * @return {@link RetrofitServiceFactory} ready to construct service instances.
     */
    private RetrofitServiceFactory getRetrofitServiceFactory() {
        Assert.notNull(beanFactory, "BeanFactory may not be null");

        if (retrofitServiceFactory == null) {
            RetrofitProperties properties = beanFactory.getBean(RetrofitProperties.class);
            Retrofit retrofit = beanFactory.getBean(Retrofit.class);

            retrofitServiceFactory = new RetrofitServiceFactory(beanFactory, retrofit, properties);
        }

        return retrofitServiceFactory;
    }
}
