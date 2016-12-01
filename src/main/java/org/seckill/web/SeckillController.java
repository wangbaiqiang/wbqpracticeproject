package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecutor;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
@Controller//@Service @Component
@RequestMapping("/seckill")//url:/模块/资源/{id}/细分   /seckill/list
public class SeckillController {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @RequestMapping(name="/list",method = RequestMethod.GET)
        public String list(Model model){
        //h获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
        //list.jsp+model= ModelAndView
            return "list";
        }
    @RequestMapping(value="/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){//@PathVariable可以不写也能自动识别，写了方便其他人看
        if(seckillId==null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if(seckill==null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    //ajax json
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    @RequestMapping(value = "/{seckillId}/{md5}/excution",method = RequestMethod.POST,produces = {"application/json;charset=UFT-8"})
    public SeckillResult<SeckillExecutor> execute(@PathVariable("seckillId") Long seckillId,@PathVariable("md5")String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone){
        //springMVC的验证信息 valid
        if(phone==null) {
            return new SeckillResult<SeckillExecutor>(false,"未注册");
        }
        SeckillResult<SeckillExecutor> result=null;
        try {
            SeckillExecutor executor = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecutor>(true,executor);
        }catch (RepeatKillException e){
            SeckillExecutor seckillExecutor=new SeckillExecutor(seckillId, SeckillStatEnum.REPEAT_KILL);
                return new SeckillResult<SeckillExecutor>(false,seckillExecutor);
        }catch (SeckillCloseException e){
            SeckillExecutor seckillExecutor=new SeckillExecutor(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecutor>(false,seckillExecutor);
        }catch (SeckillException e) {
            logger.error(e.getMessage(),e);
            SeckillExecutor seckillExecutor=new SeckillExecutor(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecutor>(false,seckillExecutor);
        }
    }
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
