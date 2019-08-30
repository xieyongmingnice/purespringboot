package com.phhc.sso.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author system
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserBaseInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    @TableId("user_base_info_id")
    private String userBaseInfoId;

    /**
     * 用户UnionId
     */
    @TableField("user_union_id")
    private String userUnionId;

    /**
     * 面向安全用药卫士小程序openId
     */
    @TableField("user_mp_open_id")
    private String userMpOpenId;

    /**
     * 面向安全用药卫士公众号openId
     */
    @TableField("user_open_id")
    private String userOpenId;

    /**
     * 所在省份
     */
    @TableField("user_province")
    private String userProvince;

    /**
     * 所在市
     */
    @TableField("user_city")
    private String userCity;

    /**
     * 所在地区
     */
    @TableField("user_area")
    private String userArea;

    /**
     * 注册医院
     */
    @TableField("user_reg_hospital")
    private String userRegHospital;

    /**
     * 注册渠道
     */
    @TableField("user_reg_channel")
    private Integer userRegChannel;

    /**
     * 是否同意使用协议（0：否，1：是）
     */
    @TableField("agree_agreement_flag")
    private Boolean agreeAgreementFlag;

    /**
     * 注册推荐来源
     */
    @TableField("user_recommend_source")
    private String userRecommendSource;

    /**
     * 是否删除
     */
    @TableField("delete_flag")
    private Boolean deleteFlag;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    @TableField("updater")
    private String updater;


}
