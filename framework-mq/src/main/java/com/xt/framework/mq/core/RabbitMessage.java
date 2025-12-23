package com.xt.framework.mq.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class RabbitMessage implements Serializable {
    private String messageId;
    private Date sendTime;
    private Object message;
    private String exchangeTypes;

    public RabbitMessage(Object message, String exchangeTypes) {
        this.message = message;
        this.exchangeTypes = exchangeTypes;
        this.messageId = UUID.randomUUID().toString();
        this.sendTime = new Date();
    }
}
