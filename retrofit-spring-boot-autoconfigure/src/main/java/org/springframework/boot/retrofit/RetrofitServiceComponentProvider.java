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

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.boot.retrofit.annotation.RetrofitService;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * Custom classpath scanner which includes interfaces that have been annotated with {@link RetrofitService}.
 * <p>
 * Since Retrofit only supports interfaces, all other types are ignored.
 *
 * @author troinine
 */
public class RetrofitServiceComponentProvider extends ClassPathScanningCandidateComponentProvider {
    public RetrofitServiceComponentProvider() {
        super(false);
        addIncludeFilter(new AnnotationTypeFilter(RetrofitService.class, true, true));
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }
}
