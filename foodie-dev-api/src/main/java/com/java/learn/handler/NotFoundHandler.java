package com.java.learn.handler;

import com.java.learn.utils.JsonResp;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 处理404异常
@RestController
public class NotFoundHandler implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public JsonResp notFound() {
        return JsonResp.error(404,"Not Found");
    }
}
