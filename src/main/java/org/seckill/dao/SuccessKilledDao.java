package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by Administrator on 2016/11/18.
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数量 0表示失败
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone")long userPhone);

    /**
     * 根据id查询successKilled并携带秒杀产品实体对象  多个产品  一对多的对应
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId ,@Param("userPhone") long userPhone);

}
