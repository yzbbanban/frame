package com.yzb.common;

import com.google.gson.Gson;
import com.yzb.entity.User;
import com.yzb.entity.UserConfirm;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 发送到短信消息队列
 *
 * @author wangban
 * @date 10:09 2018/7/28
 */
@Component
public class TopicSender<T> implements RabbitTemplate.ConfirmCallback {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * @param exchange 交换机
     * @param queue    队列
     * @param data     需要发送的信息
     */
    public void sendMessage(String exchange, String queue, T data) {
        this.amqpTemplate.convertAndSend(exchange, queue, data);

    }


    /**
     * @param exchange 交换机
     * @param queue    队列
     * @param data     需要发送的信息
     */
    public void sendMessageConfirm(String exchange, String queue, User data) {
        String id = UUID.randomUUID().toString();
        //设置唯一标识
        data.setToken(id);
        //设置token
//        redisClusterClient.set(RedisRedPacketConstant.GROUP_RED_PACKET_QUEUE_TOKEN + id, id, 60L);
        //设置确认队列
        CorrelationData correlationData = new CorrelationData(id);
        UserConfirm confirmVO = new UserConfirm();
        BeanUtils.copyProperties(data, confirmVO);
        confirmVO.setExchange(exchange);
        confirmVO.setQueue(queue);
        Gson gson = new Gson();
        //设置是否确认
//        redisClusterClient.set(RedisRedPacketConstant.GROUP_RED_PACKET_EXCHANGE_TOKEN + id, gson.toJson(confirmVO), 10L);
        //发送消息
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.convertAndSend(exchange, queue, gson.toJson(data), correlationData);
    }

    /**
     * 需要确认
     *
     * @param correlationData 序列
     * @param b               是否成功放入 exchange
     * @param s               x
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean b, @Nullable String s) {
        try {
            if (b) {
                //投放完成，删除redis信息
                logger.debug("--投放成功--> " + correlationData);
                if (correlationData != null) {
//                    redisClusterClient.delCacheByKey(RedisRedPacketConstant.GROUP_RED_PACKET_EXCHANGE_TOKEN + correlationData.getId());
                }
            } else {
                //失败则重新投放
                logger.debug("--投放失败--> " + correlationData);
//                Gson gson = new Gson();
//                String confirmMsg = redisClusterClient.get(RedisRedPacketConstant.GROUP_RED_PACKET_EXCHANGE_TOKEN + correlationData.getId());
//                if (StringUtils.isEmpty(confirmMsg)) {
//                    return;
//                }
//                UserConfirm confirmVO = gson.fromJson(confirmMsg, UserConfirm.class);
//                sendMessageConfirm(confirmVO.getExchange(), confirmVO.getQueue(), confirmVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
