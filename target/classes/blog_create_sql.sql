-- 分类信息表（tbl_category_content）
CREATE TABLE `tbl_category_info` (
`id` bigint(40) NOT NULL AUTO_INCREMENT,
`name` varchar(20) NOT NULL COMMENT '分类名称',
`number` tinyint(10) NOT NULL DEFAULT '0' COMMENT '该分类下的文章数量',
`create_by` datetime NOT NULL COMMENT '分类创建时间',
`modified_by` datetime NOT NULL COMMENT '分类修改时间',
`is_effective` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，默认为1有效，为0无效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 文章内容表（tbl_article_content）
CREATE TABLE `tbl_article_content` (
`id` bigint(40) NOT NULL AUTO_INCREMENT,
`content` text NOT NULL,
`article_id` bigint(40) NOT NULL COMMENT '对应文章ID',
`create_by` datetime NOT NULL COMMENT '创建时间',
`modifield_by` datetime NOT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 文章信息表（tbl_article_info）
CREATE TABLE `tbl_article_info` (
`id` bigint(40) NOT NULL AUTO_INCREMENT COMMENT '主键',
`title` varchar(50) NOT NULL DEFAULT '' COMMENT '文章标题',
`summary` varchar(300) NOT NULL DEFAULT '' COMMENT '文章简介，默认100个汉字以内',
`is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '文章是否置顶，0为否，1为是',
`traffic` int(10) NOT NULL DEFAULT '0' COMMENT '文章访问量',
`create_by` datetime NOT NULL COMMENT '创建时间',
`modified_by` datetime NOT NULL COMMENT '修改日期',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 文章分类表（tbl_article_category）
CREATE TABLE `tbl_article_category` (
`id` bigint(40) NOT NULL AUTO_INCREMENT,
`sort_id` bigint(40) NOT NULL COMMENT '分类id',
`article_id` bigint(40) NOT NULL COMMENT '文章id',
`create_by` datetime NOT NULL COMMENT '创建时间',
`modified_by` datetime NOT NULL COMMENT '更新时间',
`is_effective` tinyint(1) DEFAULT '1' COMMENT '表示当前数据是否有效，默认为1有效，0则无效',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 文章题图表（tbl_article_picture）
CREATE TABLE `tbl_article_picture` (
`id` bigint(40) NOT NULL AUTO_INCREMENT,
`article_id` bigint(40) NOT NULL COMMENT '对应文章id',
`picture_url` varchar(100) NOT NULL DEFAULT '' COMMENT '图片url',
`create_by` datetime NOT NULL COMMENT '创建时间',
`modified_by` datetime NOT NULL COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='这张表用来保存题图url，每一篇文章都应该有题图';