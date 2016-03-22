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
package org.springframework.boot.retrofit.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates an interface as Retrofit service.
 * <p>
 * Use this annotation to qualify a Retrofit annotated interface for auto-detection and automatic
 * instantiation.
 *
 * @author troinine
 * @see org.springframework.boot.retrofit.RetrofitServiceScan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RetrofitService {
    /**
     * Defines the name of the service bean when registered to the underlying context. If left unspecified
     * the name of the service bean is generated using {@link org.springframework.context.annotation.AnnotationBeanNameGenerator}.
     *
     * @return the name of the bean.
     */
    String name() default "";
}
