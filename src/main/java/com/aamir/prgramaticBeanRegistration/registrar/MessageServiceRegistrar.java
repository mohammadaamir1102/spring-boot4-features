package com.aamir.prgramaticBeanRegistration.registrar;

import com.aamir.prgramaticBeanRegistration.email.EmailMessageService;
import com.aamir.prgramaticBeanRegistration.sms.SmsMessageService;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

public class MessageServiceRegistrar implements BeanRegistrar {


    @Override
    public void register(BeanRegistry registry, Environment env) {

        String messageType = env.getProperty("app.message-type", "email");

        switch (messageType.strip().toLowerCase()) {

            case "email" -> registry.registerBean("messageService", EmailMessageService.class, spec -> spec
                    .description("Email message service registered via BeanRegistrar"));

            case "sms" -> registry.registerBean("messageService", SmsMessageService.class, spec -> spec
                    .description("SMS message service registered via BeanRegistrar"));

            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        }

    }
}
