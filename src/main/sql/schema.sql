/*数据库初始化脚本*/

--创建数据库
CREATE DATABASE seckill;
# 使用数据库
use seckill;
# 创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(20) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET utf8 COMMENT '秒杀库存表';

#初始化数据
insert into seckill(name,number,create_time,start_time,end_time)
  values
('你好',100,'2015-11-01 00:00:00','2015-11-01 00:00:00','2015-11-02 00:00:00'),
('1000元秒杀ipad2',200,'2015-11-01 00:00:00','2015-11-01 00:00:00','2015-11-02 00:00:00'),
('1000元秒杀小米4',300,'2015-11-01 00:00:00','2015-11-01 00:00:00','2015-11-02 00:00:00'),
('1000元秒杀小米note',400,'2015-11-01 00:00:00','2015-11-01 00:00:00','2015-11-02 00:00:00');

#秒杀成功明细表
#用户登录认证相关的信息
CREATE TABLE success_killed(
`seckill_id` BIGINT NOT NULL COMMENT '秒杀商品id',
`user_phone` BIGINT NOT NULL COMMENT '用户手机号',
`state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标示 -1:无效 0：成功 1：已付款 2：已发货 ',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间 也就是秒杀成功的时间',
PRIMARY KEY (seckill_id,user_phone),
key idx_create_time(create_time)
)ENGINE InnoDB DEFAULT CHARSET utf8 COMMENT '秒杀成功明细表';

#连接数据库的控制台
mysql -uroot -p823919