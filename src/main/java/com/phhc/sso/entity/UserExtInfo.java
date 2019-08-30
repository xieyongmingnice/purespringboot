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
 * 用户扩展信息表
 * </p>
 *
 * @author system
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserExtInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户扩展信息主键
     */
    @TableId("user_ext_info_id")
    private String userExtInfoId;

    /**
     * 用户基础信息ID
     */
    @TableField("user_base_info_id")
    private String userBaseInfoId;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 手机号
     */
    @TableField("tel_number")
    private String telNumber;

    /**
     * 头像
     */
    @TableField("profile_photo")
    private String profilePhoto;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 出生年月
     */
    @TableField("birthday")
    private LocalDateTime birthday;

    /**
     * 是否开启用药提醒  0:关   1：开
     */
    @TableField("drug_remind_switch_flag")
    private Boolean drugRemindSwitchFlag;

    /**
     * 用户最近扫码医院ID
     */
    @TableField("user_recent_hospital_id")
    private String userRecentHospitalId;

    /**
     * 是否删除（0：否，1：是）
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
