CREATE TABLE IF NOT EXISTS `user_info`
(
    `user_id`      integer PRIMARY KEY AUTO_INCREMENT,
    `username`     varchar(255),
    `password`     varchar(255),
    `role_id`      varchar(255),
    `created_by`   varchar(255),
--     `created_date` timestamp TIMESTAMP AS NULL DEFAULT NOW(), -- for MySQL
    `created_date` TIMESTAMP AS CURRENT_TIMESTAMP(), -- for H2
    `updated_by`   varchar(255),
--     `updated_date` timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- for MySQL
    `updated_date` TIMESTAMP AS CURRENT_TIMESTAMP()  -- for H2
);

CREATE TABLE IF NOT EXISTS `role`
(
    `role_id`      integer PRIMARY KEY AUTO_INCREMENT,
    `role_name`    varchar(255),
    `created_by`   varchar(255),
--     `created_date` timestamp TIMESTAMP AS NULL DEFAULT NOW(), -- for MySQL
    `created_date` TIMESTAMP AS CURRENT_TIMESTAMP(), -- for H2
    `updated_by`   varchar(255),
--     `updated_date` timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- for MySQL
    `updated_date` TIMESTAMP AS CURRENT_TIMESTAMP()  -- for H2
);

CREATE TABLE IF NOT EXISTS `permisison`
(
    `permission_id`   integer PRIMARY KEY AUTO_INCREMENT,
    `permission_name` varchar(255),
    `created_by`      varchar(255),
--     `created_date` timestamp TIMESTAMP AS NULL DEFAULT NOW(), -- for MySQL
    `created_date`    TIMESTAMP AS CURRENT_TIMESTAMP(), -- for H2
    `updated_by`      varchar(255),
--     `updated_date` timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- for MySQL
    `updated_date`    TIMESTAMP AS CURRENT_TIMESTAMP()  -- for H2
);

CREATE TABLE IF NOT EXISTS `role_permission`
(
    `role_id`       integer,
    `permission_id` integer,
    PRIMARY KEY (`role_id`, `permission_id`)
);

CREATE TABLE IF NOT EXISTS `file`
(
    `file_id`          integer PRIMARY KEY AUTO_INCREMENT,
    `file_name`        varchar(255),
    `file_type`        varchar(255),
    `status`           varchar(255),
    `last_access_by`   varchar(255),
    `last_access_time` timestamp,
    `created_by`       varchar(255),
--     `created_date` timestamp TIMESTAMP AS NULL DEFAULT NOW(), -- for MySQL
    `created_date`     TIMESTAMP AS CURRENT_TIMESTAMP(), -- for H2
    `updated_by`       varchar(255),
--     `updated_date` timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- for MySQL
    `updated_date`     TIMESTAMP AS CURRENT_TIMESTAMP()  -- for H2
);

CREATE TABLE IF NOT EXISTS `file_detail`
(
    `file_detail_id` integer PRIMARY KEY AUTO_INCREMENT,
    `row_data`       varchar(255),
    `is_header`      tinyint,
    `file_id`        integer,
    `created_by`     varchar(255),
--     `created_date` timestamp TIMESTAMP AS NULL DEFAULT NOW(), -- for MySQL
    `created_date`   TIMESTAMP AS CURRENT_TIMESTAMP(), -- for H2
    `updated_by`     varchar(255),
--     `updated_date` timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- for MySQL
    `updated_date`   TIMESTAMP AS CURRENT_TIMESTAMP()  -- for H2
);

-- ALTER TABLE `user_info`
--     ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);
--
-- ALTER TABLE `role_permission`
--     ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);
--
-- ALTER TABLE `role_permission`
--     ADD FOREIGN KEY (`permission_id`) REFERENCES `permisison` (`permission_id`);
--
-- ALTER TABLE `file_detail`
--     ADD FOREIGN KEY (`file_id`) REFERENCES `file` (`file_id`);

INSERT INTO `role`(`role_id`, `role_name`, `created_by`, `updated_by`)
VALUES (1, 'ADMIN', 'System', 'System');

INSERT INTO `role`(`role_id`, `role_name`, `created_by`, `updated_by`)
VALUES (2, 'USER', 'System', 'System');

INSERT INTO `permisison`(`permission_id`, `permission_name`, `created_by`, `updated_by`)
VALUES (1, 'VIEW', 'System', 'System');

INSERT INTO `permisison`(`permission_id`, `permission_name`, `created_by`, `updated_by`)
VALUES (2, 'UPLOAD', 'System', 'System');

INSERT INTO `permisison`(`permission_id`, `permission_name`, `created_by`, `updated_by`)
VALUES (3, 'DELETE', 'System', 'System');

INSERT INTO `user_info`(`username`, `password`, `role_id`, `created_by`, `updated_by`)
VALUES ('trungnguyen', '$2a$12$PfNRLvIoRY3Ymg6ObyhV5.IPfWbPl9mtABGUrE42gJLQjd2aeg7ui', 1, 'System', 'System');

INSERT INTO `role_permission`(`role_id`, `permission_id`)
VALUES (1, 1);

INSERT INTO `role_permission`(`role_id`, `permission_id`)
VALUES (1, 2);

INSERT INTO `role_permission`(`role_id`, `permission_id`)
VALUES (1, 3);

INSERT INTO `role_permission`(`role_id`, `permission_id`)
VALUES (2, 1);

INSERT INTO `role_permission`(`role_id`, `permission_id`)
VALUES (2, 2);



