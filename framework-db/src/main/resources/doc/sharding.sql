drop table if exists `t_order_0`;
CREATE TABLE `t_order_0`
(
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `user_id`     BIGINT UNSIGNED NOT NULL,
    `price`       decimal(10, 4) DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
drop table if exists `t_order_item_0`;
CREATE TABLE `t_order_item_0`
(
    `item_no`     BIGINT UNSIGNED NOT NULL,
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `item_name`   varchar(50)              DEFAULT NULL,
    `price`       decimal(10, 4)  not null DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`item_no`),
    INDEX `idx_order_no` (order_no)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
drop table if exists `t_order_1`;
CREATE TABLE `t_order_1`
(
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `user_id`     BIGINT UNSIGNED NOT NULL,
    `price`       decimal(10, 4) DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
drop table if exists `t_order_item_1`;
CREATE TABLE `t_order_item_1`
(
    `item_no`     BIGINT UNSIGNED NOT NULL,
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `item_name`   varchar(50)              DEFAULT NULL,
    `price`       decimal(10, 4)  not null DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`item_no`),
    INDEX `idx_order_no` (order_no)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
drop table if exists `t_order_2`;
CREATE TABLE `t_order_2`
(
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `user_id`     BIGINT UNSIGNED NOT NULL,
    `price`       decimal(10, 4) DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`order_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
drop table if exists `t_order_item_2`;
CREATE TABLE `t_order_item_2`
(
    `item_no`     BIGINT UNSIGNED NOT NULL,
    `order_no`    BIGINT UNSIGNED NOT NULL,
    `item_name`   varchar(50)              DEFAULT NULL,
    `price`       decimal(10, 4)  not null DEFAULT '0.0000',
    `create_time` datetime        NOT NULL COMMENT '创建时间',
    `update_time` datetime        NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`item_no`),
    INDEX `idx_order_no` (order_no)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;

drop table if exists `t_config`;
CREATE TABLE `t_config`
(
    `id`          int UNSIGNED NOT NULL AUTO_INCREMENT,
    `remark`      varchar(50) CHARACTER SET utf8 DEFAULT NULL,
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1
  COLLATE = utf8_unicode_ci COMMENT = '广播表';
