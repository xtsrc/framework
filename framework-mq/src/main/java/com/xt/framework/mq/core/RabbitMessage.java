package com.xt.framework.mq.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitMessage implements Serializable {
    private static final long serialVersionUID = 627080879383673726L;
    private String messageId;
    private Date sendTime;
    private Object message;
    private String exchangeTypes;
    private Long retry;

    public RabbitMessage(Object message, String exchangeTypes) {
        this.message = message;
        this.exchangeTypes = exchangeTypes;
        this.messageId = UUID.randomUUID().toString();
        this.sendTime = new Date();
        this.retry=0L;
    }
}
