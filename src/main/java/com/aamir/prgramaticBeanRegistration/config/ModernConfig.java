package com.aamir.prgramaticBeanRegistration.config;

import com.aamir.prgramaticBeanRegistration.registrar.MessageServiceRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MessageServiceRegistrar.class)
public class ModernConfig {
}
