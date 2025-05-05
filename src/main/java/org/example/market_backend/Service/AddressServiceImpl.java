package org.example.market_backend.Service;

import com.github.pagehelper.PageHelper;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Mapper.AddressMapper;
import org.example.market_backend.VO.AddressVO;
import org.example.market_backend.VO.BaseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;
import org.example.market_backend.Entity.Address;

import java.util.Date;
import java.util.List;

public class AddressServiceImpl implements AddressService {

    private AddressMapper addressMapper;
    private UserService userService;
    @Override
    public List<Address> getAddressList(BaseVO vo){
        User user = userService.checkToken(vo.getToken());
        Example example = new Example(Address.class);
        example.createCriteria().andEqualTo("userId",user.getUserId()).andEqualTo("status",1);
        example.setOrderByClause("create_time desc,is_default_address");
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return addressMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,  isolation = Isolation.READ_COMMITTED)
    public void saveAddress(AddressVO vo) {
        User user = userService.checkToken(vo.getToken());
        Address address = new Address();
        Date  date = new Date();
        BeanUtils.copyProperties(vo,address);
        address.setCreateTime(date);
        address.setUpdateTime(date);
        address.setUserId(user.getUserId());
        if(1==vo.getIsDefaultAddress()){
            Example example = new Example(Address.class);
            example.createCriteria()
                    .andEqualTo("isDefaultAddress",1)
                    .andEqualTo("userId",user.getUserId());
            Address update = new Address();
            update.setIsDefaultAddress(2);
            update.setUpdateTime(date);
            addressMapper.updateByExampleSelective(update,example);
        }
        addressMapper.insertSelective(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,  isolation = Isolation.READ_COMMITTED)
    public void updateAddress(AddressVO vo) {
        userService.checkToken(vo.getToken());
        Address address = addressMapper.selectByPrimaryKey(vo.getId());
        Assert.notNull(address,"收货地址不存在");
        Assert.isTrue(address.getStatus() ==1 ,"收货地址已被删除");
        Address update = new Address();
        //将待修改的属性赋值给update
        BeanUtils.copyProperties(vo,update);
        update.setUpdateTime(new Date());
        if(1==vo.getIsDefaultAddress()){
            Example example = new Example(Address.class);
            example.createCriteria()
                    .andEqualTo("isDefaultAddress",1)
                    .andEqualTo("id",vo.getId());
            Address entity = new Address();
            entity.setIsDefaultAddress(2);
            entity.setUpdateTime(new Date());
            addressMapper.updateByExampleSelective(entity,example);
        }
        addressMapper.updateByPrimaryKeySelective(update);
    }

    @Override
    public void delAddress(AddressVO vo) {
        userService.checkToken(vo.getToken());
        Address address = addressMapper.selectByPrimaryKey(vo.getId());
        Assert.notNull(address,"收货地址不存在");
        Assert.isTrue(address.getStatus() == 1,"收货地址已被删除");
        Address update = new Address();
        update.setStatus(2);
        update.setUpdateTime(new Date());
        update.setId(vo.getId());
        addressMapper.updateByPrimaryKeySelective(update);
    }
}
