package com.java.learn.schedule;

import com.java.learn.service.OrderService;
import com.java.learn.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CloseOvertimeOrder {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(CloseOvertimeOrder.class);

    // @Scheduled(cron = "0/3 * * * * ?")
    public void execute() {
        orderService.closeOrder();
        System.out.println("执行定时任务，当前时间为："
                + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
