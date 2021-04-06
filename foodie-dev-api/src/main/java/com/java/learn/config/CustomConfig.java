package com.java.learn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CustomConfig {

    @Value("${server.port}")
    public String SERVER_PORT;

    public String getFaceServerUrl() {
        return "http://localhost:"+ this.SERVER_PORT +"/static/faces";
    }

    // 数据分页默认参数，用于get请求，所以设置为String
    public static final String PAGE_SIZE = "10";
    public static final String PAGE = "1";

    // 默认生日
    public static final String DEFAULT_BIRTH = "1900-01-01";

    // 微信支付成功->支付中心->本平台
    // ->回调通知的url
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";

    // 支付中心地址
    public static final String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/createMerchantOrder";

    public static final String PROJECT_DIR = System.getProperty("user.dir");

    public static final String STATIC_RESOURCE = PROJECT_DIR + File.separator + "static";

    // 用户头像地址
    public static final String IMAGE_USER_FACE_LOCATION =  STATIC_RESOURCE + File.separator + "faces";

    // 上传文件验证
    public static final String FILE_NAME_REGEX = "[\\w\\u4e00-\\u9fa5^\\x00-\\xff]+\\.(png|jpg|jpeg)$";
}
