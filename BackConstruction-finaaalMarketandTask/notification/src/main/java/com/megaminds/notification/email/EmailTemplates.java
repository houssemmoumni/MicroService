package com.megaminds.notification.email;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    ORDER_CONFIRMATION("order-confirmation.html", "Order confirmation")
    ;

    private final String template;
    private final String subject;


    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
