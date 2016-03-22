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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Spring Boot configuration properties for OkHttp.
 *
 * @author troinine
 */
@ConfigurationProperties(prefix = OkHttpProperties.PREFIX)
public class OkHttpProperties {
    /**
     * Prefix for Retrofit configuration properties.
     */
    public static final String PREFIX = "spring.okhttp";

    /**
     * The default connection timeout.
     */
    public static final long DEFAULT_CONNECTION_TIMEOUT = 10000L;

    /**
     * The default read timeout.
     */
    public static final long DEFAULT_READ_TIMEOUT = 10000L;

    /**
     * The default write timeout.
     */
    public static final long DEFAULT_WRITE_TIMEOUT = 10000L;

    private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private long readTimeout = DEFAULT_READ_TIMEOUT;
    private long writeTimeout = DEFAULT_WRITE_TIMEOUT;

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }
}
