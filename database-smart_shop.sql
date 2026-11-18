/*
 Navicat Premium Dump SQL

 Source Server         : 本地智能购物平台
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : 127.0.0.1:3306
 Source Schema         : smart_shop

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 07/11/2026 18:21:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单明细主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单编号',
  `product_id` bigint(20) NOT NULL COMMENT '商品编号',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称快照',
  `product_price` decimal(10, 2) NOT NULL COMMENT '下单时商品价格',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '商品小计',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 1, '蓝牙耳机', 199.00, 1, 199.00);
INSERT INTO `order_item` VALUES (2, 1, 3, '无线鼠标', 99.00, 1, 99.00);
INSERT INTO `order_item` VALUES (3, 2, 3, '无线鼠标', 99.00, 1, 99.00);
INSERT INTO `order_item` VALUES (4, 3, 2, '机械键盘', 299.00, 1, 299.00);
INSERT INTO `order_item` VALUES (5, 3, 3, '无线鼠标', 99.00, 1, 99.00);
INSERT INTO `order_item` VALUES (6, 4, 1, '蓝牙耳机', 199.00, 3, 597.00);
INSERT INTO `order_item` VALUES (7, 4, 2, '机械键盘', 299.00, 4, 1196.00);
INSERT INTO `order_item` VALUES (8, 5, 2, '机械键盘', 299.00, 2, 598.00);
INSERT INTO `order_item` VALUES (9, 5, 1, '蓝牙耳机', 199.00, 13, 2587.00);
INSERT INTO `order_item` VALUES (10, 5, 7, 'iPhone18 pro', 10992.00, 1, 10992.00);
INSERT INTO `order_item` VALUES (11, 6, 1, '无线蓝牙耳机', 129.00, 1, 129.00);
INSERT INTO `order_item` VALUES (12, 7, 1, '无线蓝牙耳机', 129.00, 1, 129.00);
INSERT INTO `order_item` VALUES (13, 8, 40, 'LED 氛围灯带', 59.00, 1, 59.00);
INSERT INTO `order_item` VALUES (14, 9, 39, '多功能工具箱', 159.00, 1, 159.00);
INSERT INTO `order_item` VALUES (15, 10, 39, '多功能工具箱', 159.00, 1, 159.00);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单主键',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint(20) NOT NULL COMMENT '用户编号',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '订单状态：1待支付，2已支付，3已取消',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, '20261031082613466bdd32', 6, 298.00, 1, '2026-10-31 08:26:14', '2026-10-31 08:26:14');
INSERT INTO `orders` VALUES (2, '2026103110282757f31333', 6, 99.00, 1, '2026-10-31 10:28:28', '2026-10-31 10:28:28');
INSERT INTO `orders` VALUES (3, '202610311029383e8873cd', 6, 398.00, 1, '2026-10-31 10:29:38', '2026-10-31 10:29:38');
INSERT INTO `orders` VALUES (4, '20261105111125520aa68a', 6, 1793.00, 2, '2026-11-05 11:11:26', '2026-11-05 11:49:18');
INSERT INTO `orders` VALUES (5, '20261105111201c807e268', 4, 14177.00, 2, '2026-11-05 11:12:01', '2026-11-07 16:17:38');
INSERT INTO `orders` VALUES (6, '202611061923398e9454b4', 4, 129.00, 2, '2026-11-06 19:23:40', '2026-11-06 19:23:42');
INSERT INTO `orders` VALUES (7, '2026110619252154c6483e', 4, 129.00, 2, '2026-11-06 19:25:22', '2026-11-06 19:25:27');
INSERT INTO `orders` VALUES (8, '20261107174951698dbd92', 6, 59.00, 2, '2026-11-07 17:49:52', '2026-11-07 17:49:54');
INSERT INTO `orders` VALUES (9, '202611071752071fdca4ba', 6, 159.00, 2, '2026-11-07 17:52:08', '2026-11-07 17:52:10');
INSERT INTO `orders` VALUES (10, '2026110718194625f803c3', 6, 159.00, 2, '2026-11-07 18:19:46', '2026-11-07 18:19:48');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `stock` int(11) NULL DEFAULT 0,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片地址',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品分类编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '无线蓝牙耳机', 129.00, 118, '轻量入耳式设计，支持蓝牙5.3，适合通勤、运动和日常通话。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (2, '降噪头戴式耳机', 299.00, 80, '主动降噪与长续航，适合办公、学习和长途出行。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (3, '机械键盘', 269.00, 65, '87键机械键盘，手感清晰，支持有线连接，适合办公和游戏。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (4, '无线静音鼠标', 79.00, 180, '静音按键与人体工学设计，支持多设备切换。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (5, 'USB-C 扩展坞', 159.00, 95, '多接口扩展坞，支持 HDMI、USB3.0 和 PD 快充。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (6, '27英寸办公显示器', 1099.00, 35, '2K 高清显示器，低蓝光护眼，适合办公、设计和学习。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (7, '1080P 高清摄像头', 189.00, 70, '自动对焦高清摄像头，支持视频会议和在线课程。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (8, '便携式固态硬盘 1TB', 499.00, 50, '高速移动固态硬盘，体积小巧，适合备份和携带。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (9, '智能手机支架', 39.90, 220, '桌面可调节手机支架，适合视频观看和在线会议。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (10, '桌面无线充电器', 69.00, 150, '支持多种手机无线充电协议，桌面使用方便。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 1);
INSERT INTO `product` VALUES (11, '人体工学办公椅', 899.00, 24, '高背网布办公椅，支持腰部支撑和多档调节。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (12, '升降电脑桌', 1299.00, 18, '电动升降桌，久坐办公时可自由切换站立姿势。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (13, '护眼台灯', 159.00, 88, '无频闪护眼台灯，支持亮度和色温调节。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (14, '桌面收纳架', 49.90, 160, '多层桌面收纳架，适合整理书籍、文件和办公用品。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (15, '记忆棉腰靠', 89.00, 110, '贴合腰部曲线，帮助改善久坐办公时的支撑体验。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (16, '保温咖啡杯', 59.00, 200, '食品级不锈钢内胆，适合办公室和日常出行使用。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (17, '智能电热水壶', 169.00, 76, '快速烧水，支持多档温度保温，适合家庭和办公室。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (18, '便携榨汁杯', 99.00, 92, '充电式便携榨汁杯，适合制作果汁和奶昔。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (19, '家用空气净化器', 699.00, 42, '多层滤网净化空气，适合卧室、书房和客厅。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (20, '香薰加湿器', 119.00, 105, '静音加湿并支持香薰扩散，营造舒适室内环境。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (21, '纯棉四件套', 239.00, 60, '柔软亲肤纯棉面料，多种简约配色可选。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 3);
INSERT INTO `product` VALUES (22, '轻薄羽绒服', 399.00, 45, '轻量保暖羽绒服，适合秋冬通勤和户外出行。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 3);
INSERT INTO `product` VALUES (23, '双肩通勤背包', 149.00, 130, '大容量多分区背包，可放电脑和日常用品。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 3);
INSERT INTO `product` VALUES (24, '折叠晴雨伞', 39.90, 260, '便携防风折叠伞，晴雨两用，适合放入背包。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 3);
INSERT INTO `product` VALUES (25, '便携旅行收纳袋', 69.90, 145, '多尺寸旅行收纳袋，方便整理衣物和洗漱用品。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 3);
INSERT INTO `product` VALUES (26, '跑步运动鞋', 259.00, 75, '轻量缓震运动鞋，适合慢跑、健身和日常穿着。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 4);
INSERT INTO `product` VALUES (27, '智能运动手环', 199.00, 90, '记录步数、睡眠和心率，支持多种运动模式。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 4);
INSERT INTO `product` VALUES (28, '瑜伽垫', 89.00, 125, '加厚防滑瑜伽垫，适合瑜伽、拉伸和居家训练。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 4);
INSERT INTO `product` VALUES (29, '可调哑铃套装', 329.00, 35, '可调节重量哑铃，节省空间，适合家庭力量训练。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 4);
INSERT INTO `product` VALUES (30, '户外保温水壶', 109.00, 100, '大容量保温水壶，适合徒步、旅行和日常运动。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 4);
INSERT INTO `product` VALUES (31, '温和洁面乳', 79.00, 180, '温和清洁肌肤，适合日常洁面和多种肤质。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (32, '补水保湿面霜', 139.00, 115, '清爽保湿面霜，帮助维持肌肤水润状态。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (33, '防晒乳', 89.00, 160, '轻薄不黏腻防晒乳，适合日常通勤和户外活动。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (34, '护手霜礼盒', 69.00, 140, '多香型护手霜礼盒，适合作为日常使用或礼物。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (35, '电动牙刷', 199.00, 85, '声波电动牙刷，多档清洁模式，续航时间长。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (36, '旅行洗漱包', 49.90, 210, '防水多分区洗漱包，适合出差和旅行携带。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 5);
INSERT INTO `product` VALUES (37, '儿童积木套装', 129.00, 70, '益智拼搭积木，锻炼空间想象和动手能力。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (38, '桌面迷你风扇', 45.00, 155, '低噪桌面风扇，支持多档风速，适合办公和学习。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (39, '多功能工具箱', 159.00, 46, '家庭常用工具组合，适合日常安装和简单维修。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);
INSERT INTO `product` VALUES (40, 'LED 氛围灯带', 59.00, 129, '可调色温氛围灯带，适合桌面、卧室和背景装饰。', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80', 2);

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sort_order` int(11) NOT NULL DEFAULT 0,
  `enabled` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_category
-- ----------------------------
INSERT INTO `product_category` VALUES (1, '数码办公', 1, 1);
INSERT INTO `product_category` VALUES (2, '家居生活', 2, 1);
INSERT INTO `product_category` VALUES (3, '服饰出行', 3, 1);
INSERT INTO `product_category` VALUES (4, '运动健康', 4, 1);
INSERT INTO `product_category` VALUES (5, '美妆个护', 5, 1);

-- ----------------------------
-- Table structure for product_favorite
-- ----------------------------
DROP TABLE IF EXISTS `product_favorite`;
CREATE TABLE `product_favorite`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `fk_favorite_product`(`product_id`) USING BTREE,
  CONSTRAINT `fk_favorite_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for product_review
-- ----------------------------
DROP TABLE IF EXISTS `product_review`;
CREATE TABLE `product_review`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `rating` tinyint(4) NOT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_anonymous` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_review_product`(`product_id`) USING BTREE,
  INDEX `idx_review_user`(`user_id`) USING BTREE,
  CONSTRAINT `fk_review_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_review
-- ----------------------------
INSERT INTO `product_review` VALUES (1, 4, 40, 5, '好用', 0, '2026-11-07 17:31:20');

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_cart_user_product`(`user_id`, `product_id`) USING BTREE,
  INDEX `fk_cart_product`(`product_id`) USING BTREE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------
INSERT INTO `shopping_cart` VALUES (13, 7, 7, 5, '2026-11-03 00:12:56', '2026-11-03 00:12:56');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：1正常，0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户角色：0普通用户，1管理员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'zhaoliu', '$2a$10$NP0296IOVRsFUMNfrvkwAOU2POvGSoC2HvT85iRHCP9FIP84pK91C', '平台管理员', 'admin@smart-shop.local', '13800000001', 1, '2026-09-23 23:36:59', '2026-11-05 11:50:31', 1);
INSERT INTO `sys_user` VALUES (2, 'wangwu', '$2a$10$4X/zH84jlGh6h3BPpU/.Q.1Qj266CETacbnd0gYOt3C3f3oDIuQnS', '体验用户', 'user001@smart-shop.local', '13800000002', 1, '2026-09-23 23:39:12', '2026-11-05 11:50:31', 0);
INSERT INTO `sys_user` VALUES (3, 'laoliu', '$2a$10$g3qFTHm7asRC.a3VVsnV5.O2o68siVEZLin3OZKFuwCl/BjZT0e5S', '老六', 'laoliu@example.com', '13700000000', 1, '2026-10-24 15:01:00', '2026-10-24 15:01:00', 0);
INSERT INTO `sys_user` VALUES (4, 'admin', '$2a$10$mEwcjHuMFNkJq1/tdlmH3O16dhiMJcV.dRX5aNlctGcpzvghuf.5K', '管理员', 'admin@example.com', '13900000000', 1, '2026-10-25 13:35:12', '2026-10-25 13:35:35', 1);
INSERT INTO `sys_user` VALUES (5, 'laoba2', '$2a$10$iyeAUmKxhosW.oHj/Yyh7uayQ2Ue9uWYlbv2RcIXHt2MAWAP8zy32', '老八', 'laoba2@example.com', '13800000000', 1, '2026-10-25 16:31:14', '2026-10-25 16:31:14', 0);
INSERT INTO `sys_user` VALUES (6, 'newuser1', '$2a$10$LMxix4//Pm6BSvt9epNRDe15AJdNde9vdfZz51TztZLAVfVpRjf8W', '新人1', '1223344550@qq.com', '12345678000', 1, '2026-10-31 07:40:47', '2026-10-31 07:40:47', 0);
INSERT INTO `sys_user` VALUES (7, 'newuser2', '$2a$10$zUtDNj2dz575dvPhwkQAa.DExDh4efh5oLVmxiyySUcvPE8Abrwu.', '新人2', '1223344550@qq.com', '12345678000', 1, '2026-11-03 00:12:13', '2026-11-03 00:12:13', 0);

SET FOREIGN_KEY_CHECKS = 1;
