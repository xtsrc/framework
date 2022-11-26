CREATE TABLE `t_log`
(
    `id`           bigint(20) UNSIGNED                                          NOT NULL AUTO_INCREMENT,
    `user_id`      bigint(20) COMMENT '请求用户id',
    `trace_id`     varchar(50)                                                  NOT NULL COMMENT '请求唯一标识',
    `uri`          varchar(22)                                                  NOT NULL COMMENT '请求路径',
    `query_string` varchar(50) COMMENT '请求url上的参数',
    `method`       varchar(50) COMMENT '请求方式',
    `description`  varchar(200)                                                          DEFAULT NULL COMMENT '操作说明',
    `ip`           varchar(200)                                                          DEFAULT NULL COMMENT '请求ip',
    `body`         varchar(200)                                                          DEFAULT NULL COMMENT '请求体',
    `token`        varchar(200)                                                          DEFAULT NULL COMMENT '请求token',
    `return_data`  varchar(200)                                                          DEFAULT NULL COMMENT '返回结果',
    `start_time`   bigint(20) COMMENT '开始时间',
    `end_time`     bigint(20) COMMENT '结束时间',

    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`  datetime                                                     NOT NULL COMMENT '创建时间',
    `update_time`  datetime                                                     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `Index_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='日志表';

DROP DATABASE IF EXISTS `t_sy_admin`;
CREATE TABLE `t_sy_admin`
(
    `id`               bigint(20) UNSIGNED                                          NOT NULL AUTO_INCREMENT,
    `user_id`          bigint(20)                                                   NOT NULL COMMENT '会员中心员工id',
    `name`             varchar(50)                                                  NOT NULL COMMENT '管理员名称',
    `phone`            varchar(128)                                                  NOT NULL COMMENT '电话号码',
    `channel_code`     varchar(50)                                                  NOT NULL COMMENT '渠道编码',
    `channel_discount` float                                                        NOT NULL default 10.0 COMMENT '渠道折扣',
    `staff_amount`     INTEGER                                                      NOT NULL default 0 COMMENT '员工数量',
    `version`     INTEGER                                                      NOT NULL default 0 COMMENT '版本信息',
    `remark`           varchar(200)                                                          DEFAULT NULL COMMENT '备注',
    `share_url`           varchar(200)                                                          DEFAULT NULL COMMENT '分享链接',
    `status`           tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '状态(0:禁用 1:启用)',
    `is_deleted`       tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除(0:正常 1:已删除)',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
    `create_time`      datetime                                                     NOT NULL COMMENT '创建时间',
    `update_time`      datetime                                                     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    Unique KEY `Index_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='私域管理员表';
