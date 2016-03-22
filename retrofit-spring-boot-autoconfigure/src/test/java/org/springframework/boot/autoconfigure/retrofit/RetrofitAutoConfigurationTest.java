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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.retrofit.RetrofitServiceScan;
import org.springframework.boot.retrofit.annotation.RetrofitService;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link RetrofitAutoConfiguration}
 *
 * @author troinine
 */
public class RetrofitAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

    @RetrofitService
    public interface MyService {
        @GET("/hello")
        Call<Hello> sayHello();

        @GET("/hello-observable-scalar")
        Observable<String> toHelloObservable();
    }

    @RetrofitService(name = MyCustomBeanNameService.BEAN_NAME)
    public interface MyCustomBeanNameService {
        String BEAN_NAME = "myBeanName";

        @GET("/hello")
        Call<Hello> sayHello();
    }

    @Configuration
    @RetrofitServiceScan
    public static class RetrofitTestConfiguration {
        // To enable service scanning
    }

    @Before
    public void setup() {
        loadContext();
    }

    @After
    public void teardown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testRetrofitAutoConfigured() {
        Retrofit retrofit = context.getBean(Retrofit.class);

        assertThat(retrofit).isNotNull();
        assertThat(retrofit.baseUrl().toString()).isEqualTo("http://localhost/");
    }

    @Test
    public void testRetrofitAutoConfiguredWithCallAdapters() {
        Retrofit retrofit = context.getBean(Retrofit.class);

        assertThat(retrofit).isNotNull();

        // Assert that we have exactly the call adapter factories that are auto-configured
        List<CallAdapter.Factory> callAdapterFactories = retrofit.callAdapterFactories();

        // Retrofit internally adds its DefaultCallAdapterFactory
        assertThat(callAdapterFactories)
                .hasSize(2)
                .hasAtLeastOneElementOfType(RxJavaCallAdapterFactory.class);
    }

    @Test
    public void testRetrofitAutoConfiguredWithConverters() {
        Retrofit retrofit = context.getBean(Retrofit.class);

        assertThat(retrofit).isNotNull();

        // Assert that we have exactly the converter factories that are auto-configured
        List<Converter.Factory> converterFactories = retrofit.converterFactories();

        // Retroit internally adds BuildInConverters
        assertThat(converterFactories)
                .hasSize(3)
                .hasAtLeastOneElementOfType(JacksonConverterFactory.class)
                .hasAtLeastOneElementOfType(ScalarsConverterFactory.class);
    }


    @Test(expected = BeanCreationException.class)
    public void testMissingConfigrationProperties() {
        context = new AnnotationConfigApplicationContext();
        context.register(RetrofitAutoConfiguration.class);
        context.refresh();

        Retrofit retrofit = context.getBean(Retrofit.class);
    }

    @Test
    public void testMyServiceAutoConfigured() {
        MyService myService = context.getBean(MyService.class);

        assertThat(myService).isNotNull();
    }

    @Test
    public void testCustomizingRetrofitServiceBeanName() {
        MyCustomBeanNameService myCustomBeanNameService =
                (MyCustomBeanNameService) context.getBean(MyCustomBeanNameService.BEAN_NAME);

        assertThat(myCustomBeanNameService).isNotNull();
    }

    private void loadContext() {
        context = new AnnotationConfigApplicationContext();

        EnvironmentTestUtils.addEnvironment(context, "spring.retrofit.base-url:http://localhost/");

        context.register(RetrofitAutoConfiguration.class,
                RetrofitTestConfiguration.class);

        context.refresh();
    }

    private static class Hello {
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;
    }
}