package entity;

import java.io.Serializable;

/**
 * @author zx
 * @version 1.0
 * @date 2019/12/31  13:41
 */
public class Result implements Serializable{
    private boolean success;
    private String msg;

    public Result(boolean success,String msg ){
        super();
        this.success=success;
        this.msg=msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess( boolean success ) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg( String msg ) {
        this.msg = msg;
    }
}
