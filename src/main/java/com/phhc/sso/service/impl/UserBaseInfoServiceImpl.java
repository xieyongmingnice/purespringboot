package com.phhc.sso.service.impl;

import com.phhc.sso.entity.UserBaseInfo;
import com.phhc.sso.mapper.UserBaseInfoMapper;
import com.phhc.sso.service.IUserBaseInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author system
 * @since 2019-08-29
 */
@Service
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements IUserBaseInfoService {

}
