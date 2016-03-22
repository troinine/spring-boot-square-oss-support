# Spring Boot auto-configuration and starters for Retrofit and OkHttp
**Work in progress**

This module provides Spring Boot support for Square's Popular OSS libraries Retrofit 2 and OkHttp 3.

# Binaries

Binaries are not yet deployed to anywhere. You can compile them in the root of the repository easily with the included [Maven Wrapper](https://github.com/takari/maven-wrapper)  by invoking

```
$ ./mvnw clean install
```

or in Windows

```
$ ./mvnw.cmd clean install
```
# Usage

Using the whole package is rather easy. To enable Retrofit and OkHttp auto-configuration, you just delcare a dependency in your build configuration. For example in Maven projects:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>x.y.z</version>
</dependency>
```
## Enabling scanning of Retrofit services

In order to scan for Retrofit services, you have to use ```@RetrofitService``` and ```@RetrofitServiceScan``` annotations. The first one is meant for marking service interfaces as Retrofit interfaces and with the latter one you enable the auto-configuration and declare the packages to scan for ```@RetrofitService``` annotated interfaces.

As an example, to mark a Retrofit service interface

```java
package org.myapp;

import org.springframework.boot.retrofit.annotation.RetrofitService;
import retrofit2.http.GET;
import rx.Observable;

@RetrofitService
public interface HelloService {
    @GET("/hello")
    Observable<Hello> sayHello();
}
```

And to enable the scanning and auto-configuration

```java
package org.myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RetrofitServiceScan
public class Main {

    @Autowired
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
```

To check the Spring application configurations, refer to ```org.springframework.boot.autoconfigure.retrofit.RetrofitProperties``` and ```org.springframework.boot.autoconfigure.okhttp.OkHttpProperties```


