package com.xxxx.server.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.MailConstants;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IMailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitMQ配置类
 */
@Slf4j
@Configuration
public class RabbitMQConfig {


    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);

        /**
         *交换机确认回调方法
         * 1.发消息 交换机接收到了 回调
         *      1.1：correlationDate 保存回调消息的ID及相关信息
         *      1.2：交换机收到消息 true
         *      1.3：s b=null
         * 2.发消息 交换机接受失败 回调
         *      2.1：correlationDate 保存回调消息的ID及相关信息
         *      2.2：交换机收到消息 b=false
         *      2.3：s 失败的原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId=data!=null ? data.getId() : "";
            if(ack){
                log.info("{}====>消息发送成功",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("status",1).eq("msgId",msgId));
            }else{
                log.error("{}=====>消息发送失败",msgId);
            }
        });

        rabbitTemplate.setReturnsCallback((returned)->{
            log.error("消息{},被交换机{}退回，退换原因：{}，路由key：{}",
                    new String(returned.getMessage().getBody()),returned.getExchange(),
                    returned.getReplyText(),returned.getRoutingKey());
        });

        return rabbitTemplate;
    }



    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(directExchange())
                .with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

}
