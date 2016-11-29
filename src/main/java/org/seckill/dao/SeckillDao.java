package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
@Repository
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime 对应的数据库中的createtime
     * @return 如果影响行数等于>1,标表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime")Date killTime);

    /**
     *查询
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit 取多少条
     * @return
     */
    List<Seckill> queryAll(int offset, int limit);


}
