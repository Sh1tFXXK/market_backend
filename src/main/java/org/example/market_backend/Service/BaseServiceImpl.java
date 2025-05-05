package org.example.market_backend.Service;

import lombok.Getter;
import lombok.Setter;
import org.example.market_backend.Exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Setter
@Getter
public class BaseServiceImpl <T,D> implements BaseService<T,D>{

    public Mapper<T> mapper;

    @Override
    public int save(T entity) throws BusinessException {
        return mapper.insertSelective(entity);
    }
    @Override
    public int update(T entity) throws BusinessException {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(D id) throws BusinessException {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public T findById(D id) throws BusinessException {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findList(Example example) {
        return mapper.selectByExample(example);
    }

}
