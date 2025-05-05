package org.example.market_backend.Service;

import org.example.market_backend.Entity.Praise;
import org.example.market_backend.Entity.Product;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Exception.BusinessException;
import org.example.market_backend.Mapper.PraiseMapper;
import org.example.market_backend.Mapper.ProductMapper;
import org.example.market_backend.VO.PraiseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PraiseServiceImpl implements PraiseService{
    private PraiseMapper praiseMapper;
    private UserService userService;
    private ProductMapper productMapper;
    @Override
    public void praiseOrUnPraise(@Valid PraiseVO vo) {
        //检验token是否有效
        User user = userService.checkToken(vo.getToken());
        //查询商品是否存在
        Product product = productMapper.selectByPrimaryKey(vo.getProductId());
        Assert.notNull(product,"商品信息不存在");
        //查询商品是否被当前用户点赞
        Example example = new Example(Praise.class);
        example.createCriteria().andEqualTo("productId",vo.getProductId()).andEqualTo("userId",user.getUserId());
        List<Praise> praiseList = praiseMapper.selectByExample(example);
        Date date = new Date();
        if (!praiseList.isEmpty()){
            Praise praise = praiseList.get(0);
            if(praise!=null){
                //重复点赞或重复取消点赞
                Assert.isTrue(praise.getStatus()==vo.getStatus(),"重复点赞或重复取消点赞");
                BeanUtils.copyProperties(vo,praise);
                praise.setPraiseTime(date);
                praise.setUserAvatar(user.getUserAvatar());
                praise.setUserName(user.getUserName());
                praiseMapper.updateByPrimaryKeySelective(praise);
            }
        }
    }

    /**
     * 获取点赞列表
     * @param vo
     */
    @Override
    public Map<String,Object> getPraiseList(PraiseVO vo) {
        //根据商品编号查询出有效点赞数据，并按点赞时间升序排列
        Example example = new Example(Praise.class);
        example.createCriteria().andEqualTo("productId",vo.getProductId()).andEqualTo("status",1);
        example.orderBy("praiseTime");
        Map<String,Object>  map = new HashMap<>(16);
        map.put("list",praiseMapper.selectByExample(example));
        //返回值praiseStatus字段为当前商品是否被当前用户点赞，客户端用于判断页面爱心是否点亮
        // 如果token为空，则表示当前用户没有点赞，否则表示当前用户已经点赞
        if(vo.getToken().isEmpty()){
            map.put("praiseStatus",2);
            return map;
        }
        //若token不为空，说明为登录，默认为未点赞
        User user = userService.checkToken(vo.getToken());
        example.clear();
        //查询当前登录用户是否点赞过该商品
        example.createCriteria().andEqualTo("productId",vo.getProductId()).andEqualTo("userId",user.getUserId());
        List<Praise> praiseList = praiseMapper.selectByExample(example);
        //无点赞记录，点赞状态为未点赞
        if(praiseList.isEmpty()){
            map.put("praiseStatus",2);
            return map;
        }
        Praise praise = praiseList.get(0);
        if(praise==null){
            map.put("praiseStatus",2);
            return map;
        }
        //存在点赞记录，将数据库点赞状态赋值给praiseStatus并返回
        map.put("praiseStatus",praise.getStatus());
        return map;
    }

    @Override
    public int save(Praise entity) throws BusinessException {
        return 0;
    }

    @Override
    public int update(Praise entity) throws BusinessException {
        return 0;
    }

    @Override
    public int delete(Integer id) throws BusinessException {
        return 0;
    }

    @Override
    public Praise findById(Integer id) throws BusinessException {
        return null;
    }

    @Override
    public List<Praise> findList(Example example) {
        return List.of();
    }
}
