package org.seckill.dto;

/**
 * Created by Administrator on 2016/11/30.
 */
//所有ajax请求返回类型，封装json结果
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;
    //成功就会有数据
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
    //失败就会有错误信息
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SeckillResult{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
