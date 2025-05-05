package org.example.market_backend.Service;

import com.github.pagehelper.PageHelper;
import org.example.market_backend.BO.ChatBO;
import org.example.market_backend.BO.ChatSocketBO;
import org.example.market_backend.Entity.*;
import org.example.market_backend.Mapper.ChatDetailMapper;
import org.example.market_backend.Mapper.ChatListMapper;
import org.example.market_backend.Mapper.ChatMapper;
import org.example.market_backend.Mapper.ProductMapper;
import org.example.market_backend.VO.BaseVO;
import org.example.market_backend.VO.ChatDetailVO;
import org.example.market_backend.VO.ChatSocketVO;
import org.example.market_backend.VO.InitChatVO;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;
import org.example.market_backend.Utils.UUIDUtils;

import java.util.Date;
import java.util.List;

public class ChatServiceImpl implements ChatService{
    private ChatListMapper chatListMapper;
    private UserService userService;
    private ChatDetailMapper chatDetailMapper;
    private ProductMapper productMapper;
    private ChatMapper chatMapper;
    private ChatSocket chatSocket;
    @Override
    public List<ChatBO> getChatList(BaseVO vo) {
        //检验token
        User user = userService.checkToken(vo.getToken());
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return chatListMapper.selectChatList(user.getUserId());
    }

    @Override
    public List<ChatDetail> getChatDetailList(BaseVO vo) {
        //检验token
        User user = userService.checkToken(vo.getToken());
        ChatList chatList = new ChatList();
        //将未读数变为0
        chatList.setUnread(0);
        Example example = new Example(ChatList.class);
        example.createCriteria().andEqualTo("chatId",vo.getChatId()).andEqualTo("anotherUserId",user.getUserId());
        //更新未读数量
        chatListMapper.updateByExampleSelective(chatList,example);
        //更新分页查询聊天详情数据
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        Example example1 = new Example(ChatDetail.class);
        example1.createCriteria().andEqualTo("chatId",vo.getChatId());
        //降序排列
        example1.orderBy("caeateTime").desc();
        return chatDetailMapper.selectByExample(example1);
    }
    @Override
    public String init(InitChatVO vo) {
        //检验token
        User user = userService.checkToken(vo.getToken());
        //查询商品信息
        Product product = productMapper.selectByPrimaryKey(vo.getProductId());
        Assert.notNull(product,"商品信息不存在");
        //查询聊天主表是否有记录
        Example example = new Example(Chat.class);
        example.createCriteria()
                .andEqualTo("userId",user.getUserId())
                .andEqualTo("anotherUserId",vo.getToUserId())
                .andEqualTo("productId",vo.getProductId());
        List<Chat> chatList = chatMapper.selectByExample(example);
        String chatId = null;
        if(chatList.isEmpty()){
            //将用户编号与对方用户编号对调查询，再次确认是否有聊天记录
            Example example1 = new Example(Chat.class);
            example1.createCriteria()
                    .andEqualTo("anotherUserId",user.getUserId())
                    .andEqualTo("userId",vo.getToUserId())
                    .andEqualTo("productId",vo.getProductId());
            chatList =chatMapper.selectByExample(example1);
            if(chatList.isEmpty()){
                User toUser = userService.findById(vo.getToUserId());
                Assert.notNull(toUser,"对方用户信息不存在");
                chatId = UUIDUtils.getUid();
                vo.setToUserName(toUser.getUserName());
                vo.setToUserAvatar(toUser.getUserAvatar());
                initChat(vo,chatId,vo.getProductId(),user,new Date(),0);
                return chatId;
            }
        }
        Chat chat = chatList.get(0);
        if(chat != null){
            chatId  = chat.getId();
        }
        return chatId;
    }
    /**
    *初始化聊天记录
     */
    private void initChat(ChatSocketVO vo, String chatId, Integer productId, User user, Date createTime, Integer unread){
        //向聊天主表插入记录
        Chat chat = new Chat();
        chat.setAnotherUserId(vo.getToUserId());
        chat.setUserId(user.getUserId());
        chat.setId(chatId);
        chat.setProductId(productId);
        chatMapper.insertSelective(chat);
        //向聊天列表插入记录
        ChatList chatList = new ChatList();
        chatList.setAnotherUserId(vo.getToUserId());
        chatList.setAnotherUserAvatar(vo.getToUserAvatar());
        chatList.setAnotherUserName(vo.getToUserName());
        chatList.setCreateTime(createTime);
        chatList.setUpdateTime(createTime);
        chatList.setStatus(1);
        //在线
        if(chatSocket.online(user.getUserId())){
            chatList.setIsOnline(1);
        }else{
            chatList.setIsOnline(2);
        }
        chatList.setUserAvatar(user.getUserAvatar());
        chatList.setUserId(user.getUserId());
        chatList.setUserName(user.getUserName());
        chatList.setChatId(chatId);
        if(productId!=null){
            chatList.setPruductId(productId);
        }
        chatListMapper.insertSelective(chatList);
        ChatList secondChatList = new ChatList();
        secondChatList.setUserId(vo.getToUserId());
        secondChatList.setUserAvatar(vo.getToUserAvatar());
        secondChatList.setUserName(vo.getToUserName());
        secondChatList.setAnotherUserId(user.getUserId());
        secondChatList.setAnotherUserAvatar(user.getUserAvatar());
        secondChatList.setAnotherUserName(user.getUserName());
        //不在线
        if(!chatSocket.online(vo.getToUserId())){
            secondChatList.setUnread(unread);
            secondChatList.setIsOnline(2);
        }else {
            secondChatList.setIsOnline(1);
        }
        secondChatList.setChatId(chatId);
        secondChatList.setCreateTime(createTime);
        secondChatList.setUpdateTime(createTime);
        secondChatList.setStatus(1);
        if(productId!=null){
            secondChatList.setPruductId(productId);
        }
        chatListMapper.insertSelective(secondChatList);
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public boolean chat(ChatSocketVO vo){
        //检验token
        User user = userService.checkToken(vo.getToken());
        //查询聊天
        Example example = new Example(Chat.class);
        example.createCriteria()
                .andEqualTo("id",vo.getChatId());
        List<Chat> chatList = chatMapper.selectByExample(example);
        Date date = new Date();
        String chatId = null;
        //第一次聊天
        if(chatList.isEmpty()){
            //初始化聊天
            chatId = UUIDUtils.getUid();
            initChat(vo,chatId,null,user,date,1);
        }else{
            Chat chat = chatList.get(0);
            Assert.notNull(chat,"聊天信息不存在");
            chatId = chat.getId();
            //不在线
            if(!chatSocket.online(vo.getToUserId())){
                Example example1 = new Example(ChatList.class);
                example1.createCriteria()
                        .andEqualTo("chatId",chatId)
                        .andEqualTo("userId",user.getUserId());
                List<ChatList> chatList1 = chatListMapper.selectByExample(example1);
                Assert.notEmpty(chatList1,"聊天列表信息不存在");
                ChatList chatList2 = chatList1.get(0);
                Assert.notNull(chatList2,"聊天列表信息不存在");
                ChatList update = new ChatList();
                update.setUnread(chatList2.getUnread()+1);
                update.setId(chatList2.getId());
                update.setUpdateTime(date);
                chatListMapper.updateByPrimaryKeySelective(update);
            }
            //将最后一条消息标识去掉
            Example detailExample = new Example(ChatDetail.class);
            detailExample.createCriteria()
                    .andEqualTo("chatId",chatId)
                    .andEqualTo("isLastest",1);
            ChatDetail update = new ChatDetail();
            update.setIsLastest(0);
            update.setUpdateTime(date);
            chatDetailMapper.updateByExampleSelective(update,detailExample);
        }
        //向处理聊天详情记录
        ChatDetail chatDetail = new ChatDetail();
        chatDetail.setChatId(chatId);
        chatDetail.setContent(vo.getContent());
        chatDetail.setCaeateTime(date);
        chatDetail.setIsLastest(1);
        chatDetail.setUpdateTime(date);
        chatDetail.setUserAvatar(user.getUserAvatar());
        chatDetail.setUserName(user.getUserName());
        //插入聊天记录
        chatDetailMapper.insertSelective(chatDetail);
        return true;
    }

    /**
     *初始化聊天记录
     */
    private void initChat(ChatSocketBO bo, String chatId, Integer productId, User user, Date createTime, Integer unread){
        //向聊天主表插入记录
        Chat chat = new Chat();
        chat.setAnotherUserId(bo.getToUserId());
        chat.setUserId(user.getUserId());
        chat.setId(chatId);
        chat.setProductId(productId);
        chatMapper.insertSelective(chat);
        //向聊天列表插入记录
        ChatList chatList = new ChatList();
        chatList.setAnotherUserId(bo.getToUserId());
        chatList.setAnotherUserAvatar(bo.getToUserAvatar());
        chatList.setAnotherUserName(bo.getToUserName());
        chatList.setCreateTime(createTime);
        chatList.setUpdateTime(createTime);
        chatList.setStatus(1);
        //在线
        if(chatSocket.online(user.getUserId())){
            chatList.setIsOnline(1);
        }else{
            chatList.setIsOnline(2);
        }
        chatList.setUserAvatar(user.getUserAvatar());
        chatList.setUserId(user.getUserId());
        chatList.setUserName(user.getUserName());
        chatList.setChatId(chatId);
        if(productId!=null){
            chatList.setPruductId(productId);
        }
        chatListMapper.insertSelective(chatList);
        ChatList secondChatList = new ChatList();
        secondChatList.setUserId(bo.getToUserId());
        secondChatList.setUserAvatar(bo.getToUserAvatar());
        secondChatList.setUserName(bo.getToUserName());
        secondChatList.setAnotherUserId(user.getUserId());
        secondChatList.setAnotherUserAvatar(user.getUserAvatar());
        secondChatList.setAnotherUserName(user.getUserName());
        //不在线
        if(!chatSocket.online(bo.getToUserId())){
            secondChatList.setUnread(unread);
            secondChatList.setIsOnline(2);
        }else {
            secondChatList.setIsOnline(1);
        }
        secondChatList.setChatId(chatId);
        secondChatList.setCreateTime(createTime);
        secondChatList.setUpdateTime(createTime);
        secondChatList.setStatus(1);
        if(productId!=null){
            secondChatList.setPruductId(productId);
        }
        chatListMapper.insertSelective(secondChatList);
    }
}
