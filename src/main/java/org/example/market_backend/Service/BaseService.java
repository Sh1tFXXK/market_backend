package org.example.market_backend.Service;

import org.example.market_backend.Exception.BusinessException;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 基础service
 *
 */
public interface BaseService <T,D>{
    /**
     * 保存
     * @param entity
     * @return
     * @throw BusinessException
     */
    int save (T entity) throws BusinessException;
    /**
     * 更新
     * @param entity
     * @return
     * @throw BusinessException
     */
    int update (T entity) throws BusinessException;
    /**
     * 删除
     * @param id
     * @return
     * @throw BusinessException
     */
    int delete (D id) throws BusinessException;
    /**
     * 查询
     * @param id
     * @return
     * @throw BusinessException
     */
    T findById (D id) throws BusinessException;
    /**
     * 查询列表
     * @param example
     * @return
     * @throw BusinessException
     */
     List<T> findList(Example example) ;
}
