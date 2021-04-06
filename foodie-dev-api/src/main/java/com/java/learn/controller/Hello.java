package com.java.learn.controller;

import com.java.learn.utils.JsonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ApiIgnore
@RestController
public class Hello {
    private final static Logger logger = LoggerFactory.getLogger(Hello.class);

    @GetMapping("/hello")
    public Object hello() {
        logger.debug("hello~");
        logger.info("hello~");
        logger.warn("hello~");
        logger.error("hello~");
        return "hello world!";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "Lumos");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        // session.removeAttribute("userInfo");
        return JsonResp.success();
    }
}
