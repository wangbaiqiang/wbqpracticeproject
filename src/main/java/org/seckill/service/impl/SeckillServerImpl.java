package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 * @Component @service @Dao @Controller
 */
@Service
public class SeckillServerImpl implements SeckillService {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
        //用于混淆md5，不让客户端能猜到我们的值
    private final String salt="akjdjKJKJD!kjdkljklfjkljllieeiwq*@*#(";
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill==null) {
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //当前时间的毫秒值
        Date nowTime=new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()) {
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
    private String getMD5(long seckillId){
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Transactional
    /**使用注解控制事务的优点
     * 1.开发团队打成一致 编程风格
     * 2.保证事务方法的执行时间尽可能的短，不要穿插其他的RPc/HTTP网络请求
     * 3.不是所有的方法都需要事务 只有一条操作和只读操作时不需要事务
     *
     * */
    public SeckillExecutor executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5==null||!md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑 减库存 + 记录购买行为
        Date nowTime=new Date();
        try {//这里我们要trycatch 因为可能是别的异常 可能在insert时 超时或者数据库连接断开了等问题
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if(updateCount<=0) {
                throw new SeckillCloseException("seckill is closed");
            }else{
                //记录购买行为
                int insertCount= successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount<=0) {
                    throw new RepeatKillException("seckill repeat");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecutor(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        }catch (Exception e){//整体的异常如果不是上面的两种异常
            logger.error(e.getMessage(),e);
            //所有的编译期异常转化成运行期异常 这样能让 事务能够rollback
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}
