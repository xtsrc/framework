CREATE TABLE `t_order_0`
(
    `id`          bigint(200) UNSIGNED NOT NULL,
    `user_id`          bigint(200) UNSIGNED NOT NULL,
    `order_no`    varchar(100)   DEFAULT NULL,
    `price`       decimal(10, 4) DEFAULT '0.0000',
    `create_time` datetime             NOT NULL COMMENT '创建时间',
    `update_time` datetime             NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `t_order_item_0`
(
    `id`     bigint(100)    NOT NULL,
    `order_no`    varchar(200)   NOT NULL,
    `item_name`   varchar(50)             DEFAULT NULL,
    `price`       decimal(10, 4) not null DEFAULT '0.0000',
    `create_time` datetime       NOT NULL COMMENT '创建时间',
    `update_time` datetime       NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;

CREATE TABLE `t_config`
(
    `id`          bigint(30) NOT NULL,
    `remark`      varchar(50) CHARACTER SET utf8 DEFAULT NULL,
    `create_time` datetime   NOT NULL COMMENT '创建时间',
    `update_time` datetime   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8  COLLATE = utf8_unicode_ci COMMENT = '广播表';
