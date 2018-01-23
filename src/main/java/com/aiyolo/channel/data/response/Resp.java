package com.aiyolo.channel.data.response;

/**
 * @description 统一回复报文回复
 * @date 2018年1月16日 下午15:52
 */
public class Resp {


    /**
     * 请求接口标识
     */
    private String action;
    /**
     * 错误码
     */
    private String error;
    /**
     * 回复内容
     */
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    public Resp(String action, String error){
        this.action =action;
        this.error =error;
    }

    public Resp(String action, String error, Object data){
        this.action = action;
        this.error =error;
        this.data =data;
    }

    /**
     * @description 绑定成功回复
     * @param action
     * @return
     */
    public static Resp ok(String action,Object data){
        return new Resp(action,"",data);
    }

    /**
     * @descriptin 失败回复
     * @param action
     * @return
     */
    public static Resp error(String action){
        return new Resp(action,"400");
    }


}
