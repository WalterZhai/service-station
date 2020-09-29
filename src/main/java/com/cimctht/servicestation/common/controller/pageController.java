package com.cimctht.servicestation.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class pageController {

    @RequestMapping(value="/topage")
    public String topage(HttpServletRequest request) {
        String url = request.getParameter("url");
        url = url.substring(0, url.lastIndexOf("."));
        url = url.replace(".","/");
        return url;
    }

}
