package com.example.lmsbackend.config.security.aop;

import com.example.lmsbackend.enums.PermissionEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auth {
    PermissionEnum[] permission();
    boolean selfCheck() default false;
}
