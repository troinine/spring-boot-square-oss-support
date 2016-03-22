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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OkHttpAutoConfiguration}
 *
 * @author troinine
 */
public class OkHttpAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

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

    public static class MyOkHttpConfiguration {
        public OkHttpClient mockClient = mock(OkHttpClient.class);

        @Bean
        public OkHttpClient okHttpClient() {
            return mockClient;
        }
    }

    @Test
    public void testOkHttpClientAutoConfigured() {
        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient.connectTimeoutMillis()).isEqualTo((int)OkHttpProperties.DEFAULT_CONNECTION_TIMEOUT);
        assertThat(okHttpClient.readTimeoutMillis()).isEqualTo((int)OkHttpProperties.DEFAULT_READ_TIMEOUT);
        assertThat(okHttpClient.writeTimeoutMillis()).isEqualTo((int)OkHttpProperties.DEFAULT_WRITE_TIMEOUT);
    }

    @Test
    public void testOkHttpClientAutoConfiguredWithCustomProperties() {
        context = new AnnotationConfigApplicationContext();
        context.register(OkHttpAutoConfiguration.class);
        EnvironmentTestUtils.addEnvironment(context, "spring.okhttp.connection-timeout:500");
        EnvironmentTestUtils.addEnvironment(context, "spring.okhttp.read-timeout:600");
        EnvironmentTestUtils.addEnvironment(context, "spring.okhttp.write-timeout:700");
        context.refresh();

        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);

        assertThat(okHttpClient).isNotNull();
        assertThat(okHttpClient.connectTimeoutMillis()).isEqualTo(500);
        assertThat(okHttpClient.readTimeoutMillis()).isEqualTo(600);
        assertThat(okHttpClient.writeTimeoutMillis()).isEqualTo(700);
    }

    @Test
    public void testOkHttpClientOverridingBean() {
        context = new AnnotationConfigApplicationContext();
        context.register(OkHttpAutoConfiguration.class, MyOkHttpConfiguration.class);
        context.refresh();

        OkHttpClient okHttpClient = context.getBean(OkHttpClient.class);
        MyOkHttpConfiguration myOkHttpConfiguration = context.getBean(MyOkHttpConfiguration.class);

        assertThat(okHttpClient).isEqualTo(myOkHttpConfiguration.mockClient);
    }

    private void loadContext() {
        context = new AnnotationConfigApplicationContext();

        context.register(OkHttpAutoConfiguration.class);

        context.refresh();
    }
}