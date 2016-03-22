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
package org.springframework.boot.autoconfigure.retrofit;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Retrofit 2.
 *
 * @author troinine
 */
@Configuration
@ConditionalOnClass(Retrofit.class)
@EnableConfigurationProperties(RetrofitProperties.class)
public class RetrofitAutoConfiguration {

    @Autowired(required = false)
    private List<CallAdapter.Factory> callAdapterFactories = Collections.emptyList();

    @Autowired(required = false)
    private List<Converter.Factory> converterFactories = Collections.emptyList();

    @Autowired(required = false)
    private OkHttpClient okHttpClient;

    @Autowired
    private RetrofitProperties retrofitProperties;

    @Bean
    public Retrofit retrofit() {
        validate(retrofitProperties);

        Retrofit.Builder builder = new Retrofit.Builder();

        for (CallAdapter.Factory factory : callAdapterFactories) {
            builder.addCallAdapterFactory(factory);
        }

        for (Converter.Factory factory : converterFactories) {
            builder.addConverterFactory(factory);
        }

        if (okHttpClient != null) {
            builder.client(okHttpClient);
        }

        builder.baseUrl(retrofitProperties.getBaseUrl());

        return builder.build();
    }

    private void validate(RetrofitProperties retrofitProperties) {
        try {
            new URL(retrofitProperties.getBaseUrl());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "The given base URL " + retrofitProperties.getBaseUrl() + " is not valid", e);
        }
    }

    @Configuration
    @ConditionalOnClass(RxJavaCallAdapterFactory.class)
    public static class RxJavaCallAdapterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public RxJavaCallAdapterFactory rxJavaCallAdapterFactory() {
            return RxJavaCallAdapterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(JacksonConverterFactory.class)
    public static class JacksonConverterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public JacksonConverterFactory jacksonConverterFactory() {
            return JacksonConverterFactory.create();
        }
    }

    @Configuration
    @ConditionalOnClass(ScalarsConverterFactory.class)
    public static class ScalarsConverterFactoryConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public ScalarsConverterFactory scalarsConverterFactory() {
            return ScalarsConverterFactory.create();
        }
    }
}
