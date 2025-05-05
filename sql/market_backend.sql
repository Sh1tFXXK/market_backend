-- 创建数据库
CREATE DATABASE IF NOT EXISTS `market_backend` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `market_backend`;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `sign` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `user_avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `source` tinyint(4) DEFAULT '1' COMMENT '来源：1-H5',
  `login_counts` int(11) DEFAULT '0' COMMENT '登录次数',
  `last_login_time` bigint(20) DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态：1-正常，2-锁定',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `device_id` varchar(100) DEFAULT NULL COMMENT '设备ID',
  `client_version` varchar(20) DEFAULT NULL COMMENT '客户端版本',
  `token` varchar(100) DEFAULT NULL COMMENT '用户令牌',
  `token_expired` bigint(20) DEFAULT NULL COMMENT 'token过期时间',
  `address` text COMMENT '收货地址',
  `login_status` tinyint(4) DEFAULT '2' COMMENT '登录状态：1-已登录，2-未登录',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品表
CREATE TABLE IF NOT EXISTS `product` (
  `product_id` varchar(64) NOT NULL COMMENT '商品ID',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `product_desc` text COMMENT '商品描述',
  `product_price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `product_status` tinyint(4) DEFAULT '1' COMMENT '状态：1-在售，2-已售，3-下架',
  `product_type_id` varchar(64) DEFAULT NULL COMMENT '商品类型ID',
  `user_id` varchar(64) NOT NULL COMMENT '发布用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `product_images` text COMMENT '商品图片',
  `view_counts` int(11) DEFAULT '0' COMMENT '浏览次数',
  `like_counts` int(11) DEFAULT '0' COMMENT '点赞次数',
  PRIMARY KEY (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_type_id` (`product_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 商品类型表
CREATE TABLE IF NOT EXISTS `product_type` (
  `type_id` varchar(64) NOT NULL COMMENT '类型ID',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  `type_desc` varchar(255) DEFAULT NULL COMMENT '类型描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类型表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
  `order_id` varchar(64) NOT NULL COMMENT '订单ID',
  `product_id` varchar(64) NOT NULL COMMENT '商品ID',
  `buyer_id` varchar(64) NOT NULL COMMENT '买家ID',
  `seller_id` varchar(64) NOT NULL COMMENT '卖家ID',
  `order_status` tinyint(4) DEFAULT '1' COMMENT '状态：1-待支付，2-已支付，3-已取消，4-已完成',
  `order_price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `address_id` varchar(64) DEFAULT NULL COMMENT '收货地址ID',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`order_id`),
  KEY `idx_buyer_id` (`buyer_id`),
  KEY `idx_seller_id` (`seller_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 点赞表
CREATE TABLE IF NOT EXISTS `praise` (
  `praise_id` varchar(64) NOT NULL COMMENT '点赞ID',
  `product_id` varchar(64) NOT NULL COMMENT '商品ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`praise_id`),
  UNIQUE KEY `idx_user_product` (`user_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment_reply` (
  `comment_id` varchar(64) NOT NULL COMMENT '评论ID',
  `product_id` varchar(64) NOT NULL COMMENT '商品ID',
  `user_id` varchar(64) NOT NULL COMMENT '评论用户ID',
  `comment_content` text NOT NULL COMMENT '评论内容',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父评论ID',
  `reply_user_id` varchar(64) DEFAULT NULL COMMENT '回复用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`comment_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论回复表';

-- 聊天列表
CREATE TABLE IF NOT EXISTS `chat_list` (
  `id` varchar(64) NOT NULL COMMENT '聊天列表ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `chat_with_user_id` varchar(64) NOT NULL COMMENT '聊天对象用户ID',
  `last_message` text COMMENT '最后一条消息',
  `unread_count` int(11) DEFAULT '0' COMMENT '未读消息数',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_chat_with` (`user_id`,`chat_with_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天列表';

-- 聊天详情
CREATE TABLE IF NOT EXISTS `chat_detail` (
  `message_id` varchar(64) NOT NULL COMMENT '消息ID',
  `chat_id` varchar(64) NOT NULL COMMENT '聊天ID',
  `from_user_id` varchar(64) NOT NULL COMMENT '发送用户ID',
  `to_user_id` varchar(64) NOT NULL COMMENT '接收用户ID',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` tinyint(4) DEFAULT '1' COMMENT '消息类型：1-文本，2-图片，3-语音',
  `read_status` tinyint(4) DEFAULT '0' COMMENT '读取状态：0-未读，1-已读',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_id`),
  KEY `idx_chat_id` (`chat_id`),
  KEY `idx_from_user_id` (`from_user_id`),
  KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天详情';

-- 地址表
CREATE TABLE IF NOT EXISTS `address` (
  `address_id` varchar(64) NOT NULL COMMENT '地址ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `receiver_name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `city` varchar(50) NOT NULL COMMENT '城市',
  `district` varchar(50) NOT NULL COMMENT '区县',
  `detail_address` text NOT NULL COMMENT '详细地址',
  `is_default` tinyint(4) DEFAULT '0' COMMENT '是否默认：0-否，1-是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`address_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址表';

-- 初始化商品类型数据
INSERT INTO `product_type` (`type_id`, `type_name`, `type_desc`, `create_time`, `update_time`)
VALUES
('1', '电子产品', '手机、电脑、平板等电子设备', NOW(), NOW()),
('2', '服装鞋帽', '衣服、鞋子、帽子等服饰类', NOW(), NOW()),
('3', '图书音像', '书籍、CD、DVD等', NOW(), NOW()),
('4', '家居用品', '家具、装饰品等', NOW(), NOW()),
('5', '运动户外', '运动器材、户外装备等', NOW(), NOW()),
('6', '美妆个护', '化妆品、护肤品等', NOW(), NOW()),
('7', '其他', '其他类型的二手商品', NOW(), NOW()); 