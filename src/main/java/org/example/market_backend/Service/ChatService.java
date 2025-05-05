package org.example.market_backend.Service;

import org.example.market_backend.VO.BaseVO;
import org.example.market_backend.VO.ChatSocketVO;
import org.example.market_backend.VO.InitChatVO;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {
    List getChatList(BaseVO vo);

    List getChatDetailList(BaseVO vo);

    String init(InitChatVO vo);

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    boolean chat (ChatSocketVO vo);
}
