package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 站在“使用者”的角度设计接口
 * 1.方法定义的粒度
 * 2.参数 简单  直接
 * 3.返回类型 return 友好的类型 /异常
 * Created by Administrator on 2016/11/22.
 */
public interface SeckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时 输出秒杀地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 如果用户的url被篡改了就拒绝
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecutor executeSeckill(long seckillId, long userPhone, String md5)
    throws SeckillException,RepeatKillException,SeckillCloseException;
}
