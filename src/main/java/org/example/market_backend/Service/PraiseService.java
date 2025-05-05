package org.example.market_backend.Service;

import jakarta.validation.Valid;
import org.example.market_backend.Entity.Praise;
import org.example.market_backend.VO.PraiseVO;


import java.util.Map;

public interface PraiseService extends BaseService<Praise,Integer> {
    void praiseOrUnPraise(@Valid PraiseVO vo);

    Map<String,Object> getPraiseList(PraiseVO vo);
}
