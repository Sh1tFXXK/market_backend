package org.example.market_backend.controller;

import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.AddressService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.AddressVO;
import org.example.market_backend.VO.BaseVO;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    private AddressService addressService;
    private User user;

    /**
     * 获取地址列表查询
     */
    @RequestMapping(value = "getAddressList", method = RequestMethod.POST)
    public ResponseBO getAddressList(BaseVO vo) {
        String token = checkToken();
        vo.setToken(token);
        return ResponseBO.successPageInfo(addressService.getAddressList(vo));
    }

    /**
     * 添加地址
     */
    @RequestMapping(value = "saveAddress", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO saveAddress(@Valid @RequestBody AddressVO vo) {
        String token = checkToken();
        vo.setToken(token);
        addressService.saveAddress(vo);
        return ResponseBO.success();
    }
    @RequestMapping(value = "updateAddress" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO updateAddress(@RequestBody AddressVO vo) {
        String token = checkToken();
        Assert.isNull(vo.getId(), "地址ID不能为空");
        vo.setToken(token);
        addressService.updateAddress(vo);
        return ResponseBO.success();
    }
    @RequestMapping(value = "delAddress" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO delAddress(@RequestBody AddressVO vo) {
        String token = checkToken();
        Assert.isNull(vo.getId(), "地址ID不能为空");
        vo.setToken(token);
        addressService.delAddress(vo);
        return ResponseBO.success();
    }
    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }

}
