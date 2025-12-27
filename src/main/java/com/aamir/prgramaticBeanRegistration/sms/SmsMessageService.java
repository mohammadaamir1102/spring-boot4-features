package com.aamir.prgramaticBeanRegistration.sms;

import com.aamir.prgramaticBeanRegistration.service.MessageService;

public class SmsMessageService implements MessageService {

    @Override
    public String getMessage() {
        return " \uD83D\uDCF1 This is an SMS Message Service";
    }

    @Override
    public String getServiceType() {
        return "SMS";
    }
}
