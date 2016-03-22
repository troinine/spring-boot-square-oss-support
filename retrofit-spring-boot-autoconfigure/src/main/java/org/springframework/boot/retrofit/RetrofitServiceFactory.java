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

import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.retrofit.RetrofitProperties;
import retrofit2.Retrofit;

/**
 * Factory for constructing {@link Retrofit} service instances.
 *
 * @author troinine
 */
public class RetrofitServiceFactory {
    private final BeanFactory beanFactory;
    private final Retrofit defaultRetrofit;
    private final RetrofitProperties properties;

    public RetrofitServiceFactory(BeanFactory beanFactory, Retrofit defaultRetrofit, RetrofitProperties properties) {
        this.beanFactory = beanFactory;
        this.defaultRetrofit = defaultRetrofit;
        this.properties = properties;
    }

    public <T> T createServiceInstance(Class<T> serviceClass, String beanName) {
        Retrofit retrofit = getConfiguredRetrofit(beanName);

        return retrofit.create(serviceClass);
    }

    private Retrofit getConfiguredRetrofit(String beanName) {
        return defaultRetrofit;
    }

}
