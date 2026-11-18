USE smart_shop;

CREATE TABLE IF NOT EXISTS user_address (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  receiver_name VARCHAR(50) NOT NULL,
  receiver_phone VARCHAR(20) NOT NULL,
  province VARCHAR(50) NOT NULL,
  city VARCHAR(50) NOT NULL,
  district VARCHAR(50) NOT NULL,
  detail_address VARCHAR(200) NOT NULL,
  is_default TINYINT(1) NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_address_user(user_id),
  CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS site_message (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NULL,
  title VARCHAR(100) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  message_type VARCHAR(30) NOT NULL DEFAULT 'SYSTEM',
  is_read TINYINT(1) NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_message_user(user_id), KEY idx_message_read(user_id,is_read),
  CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS point_account (
  user_id BIGINT NOT NULL,
  balance INT NOT NULL DEFAULT 0,
  total_earned INT NOT NULL DEFAULT 0,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id),
  CONSTRAINT fk_point_account_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS point_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  change_amount INT NOT NULL,
  reason VARCHAR(200) NOT NULL,
  order_id BIGINT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id), KEY idx_point_record_user(user_id),
  CONSTRAINT fk_point_record_user FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 如果 product_review 已经存在这些列，请跳过对应 ALTER；MySQL 5.7 不支持 ADD COLUMN IF NOT EXISTS。
ALTER TABLE orders ADD COLUMN address_id BIGINT NULL;
ALTER TABLE product_review ADD COLUMN audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '0待审核 1通过 2拒绝';
ALTER TABLE product_review ADD COLUMN audit_reason VARCHAR(255) NULL;
ALTER TABLE product_review ADD COLUMN audit_time DATETIME NULL;

INSERT INTO point_account(user_id,balance,total_earned)
SELECT id,0,0 FROM sys_user u WHERE NOT EXISTS (SELECT 1 FROM point_account p WHERE p.user_id=u.id);
