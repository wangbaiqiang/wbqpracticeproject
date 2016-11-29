package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2016/11/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                       "classpath:spring/spring-service.xml"})
public class SeckillServerImplTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
        private SeckillService seckillService;
    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);
    }

    @Test
    public void testGetById() throws Exception {
        long seckillId=1001;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillUrl() throws Exception {
        long seckillId=1001;
        Exposer url = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}",url);
    }

    @Test
    public void testExecuteSeckill() throws Exception {
        long id=1001;
        long phone=18233565887L;
        String md5="";
        SeckillExecutor executor = seckillService.executeSeckill(id, phone, md5);
        logger.info("executor={}",executor);

    }
        }