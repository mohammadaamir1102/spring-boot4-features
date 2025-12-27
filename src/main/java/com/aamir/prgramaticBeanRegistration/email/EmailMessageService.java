package com.aamir.prgramaticBeanRegistration.email;

import com.aamir.prgramaticBeanRegistration.service.MessageService;

public class EmailMessageService implements MessageService {

    @Override
    public String getMessage() {
        return " \uD83D\uDCE7 This is an Email Message Service";
    }

    @Override
    public String getServiceType() {
        return "EMAIL";
    }
}
