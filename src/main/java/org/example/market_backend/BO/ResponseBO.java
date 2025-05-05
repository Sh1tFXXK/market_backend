package org.example.market_backend.BO;

import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;
import org.example.market_backend.Entity.Address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.StructuredTaskScope;

@Setter
@Getter
public class ResponseBO {
    private int code;
    private String msg;
    private Map<String, Object> result;
    private String requestId;
    private long  timestamp;
    public static ResponseBO success() {
        ResponseBO responseBO = new ResponseBO();
        responseBO.setCode(0);
        responseBO.setMsg("success");
        responseBO.setTimestamp(System.currentTimeMillis());
        responseBO.setRequestId(String.valueOf(System.currentTimeMillis()));
        return responseBO;

    }
    // 在 ResponseBO 类中添加
    public static ResponseBO success(Map<String, Object> data) {
        ResponseBO responseBO = new ResponseBO();
        responseBO.setCode(0);
        responseBO.setMsg("success");
        responseBO.setTimestamp(System.currentTimeMillis());
        responseBO.setRequestId(String.valueOf(System.currentTimeMillis()));
        responseBO.setResult(data);
        return responseBO;
    }


    /**
     * 分页数据封装
     * @param lsit
     * 数据列表
     * @return
     */
    public static ResponseBO successPageInfo(List<?> lsit)  {
        Map<String,Object> map = new HashMap<>(16);
        map.put("list",lsit);
        map.put("total",Math.toIntExact(new PageInfo<>(lsit).getTotal()));
        return  ResponseBO.success(map);
    }
}
