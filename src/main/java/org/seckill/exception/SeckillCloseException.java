package org.seckill.exception;

/**
 * 如果关闭了秒杀 还要进行秒杀抛出的异常（秒杀关闭异常）
 * Created by Administrator on 2016/11/22.
 */
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
