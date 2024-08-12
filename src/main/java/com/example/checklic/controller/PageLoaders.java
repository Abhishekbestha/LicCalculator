package com.example.checklic.controller;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageLoaders {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(HttpServletRequest request) throws JSONException {
        String config = request.getParameter("config");
        if (config == null || config.isEmpty() || !config.equals("D91D37B07259EDECDC0364DCAE6E3E6874D3FFFDCFD41D04ABD3B4E13DD3196A")) {
            JSONObject json = new JSONObject();
            json.put("ErrorMsg", "Unauthorized Access");
            json.put("ErrorCode", "401");
            ModelAndView errorView = new ModelAndView("error");
            errorView.addObject("errorJson", json.toString());
            return errorView;
        } else {
            ModelAndView mv = new ModelAndView("index");
            return mv;
        }
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexx(HttpServletRequest request) throws JSONException {
        String config = request.getParameter("config");
        if (config == null || config.isEmpty() || !config.equals("D91D37B07259EDECDC0364DCAE6E3E6874D3FFFDCFD41D04ABD3B4E13DD3196A")) {
            JSONObject json = new JSONObject();
            json.put("ErrorMsg", "Unauthorized Access");
            json.put("ErrorCode", "401");
            ModelAndView errorView = new ModelAndView("error");
            errorView.addObject("errorJson", json.toString());
            return errorView;
        } else {
            ModelAndView mv = new ModelAndView("index");
            return mv;
        }
    }
}
