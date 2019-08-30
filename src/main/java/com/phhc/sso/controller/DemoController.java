package com.phhc.sso.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phhc.sso.entity.GuidanceInfo;
import com.phhc.sso.service.IGuidanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请填写类描述
 * @author xiym
 * @date 2019/8/29
 * @since
 **/
@RequestMapping("/demo")
@RestController
public class DemoController {
    @Autowired
    IGuidanceInfoService guidanceInfoService;

    @RequestMapping("/test")
    public String demo(){
        GuidanceInfo guidanceInfo = new GuidanceInfo();
        guidanceInfo.setGuidanceId("03ec8588-36ce-4b5d-857b-612e7a75f202");
        Wrapper<GuidanceInfo> wrapper = new QueryWrapper<>(guidanceInfo);
        return guidanceInfoService.getOne(wrapper).getGuidanceAliasName();
    }
}
