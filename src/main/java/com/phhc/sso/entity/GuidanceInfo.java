package com.phhc.sso.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 指导单信息表；
 * </p>
 *
 * @author system
 * @since 2019-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GuidanceInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键  指导单id
     */
    @TableField("guidance_id")
    private String guidanceId;

    /**
     * 用户id
     */
    @TableField("user_base_id")
    private String userBaseId;

    /**
     * 患者id
     */
    @TableField("patient_id")
    private String patientId;

    /**
     * 处方时间
     */
    @TableField("prescription_time")
    private LocalDateTime prescriptionTime;

    /**
     * 指导单处方ID（多个处方ID拼接）
     */
    @TableField("guidance_list_id")
    private String guidanceListId;

    /**
     * 医院名称
     */
    @TableField("hospital_name")
    private String hospitalName;

    /**
     * 医院id
     */
    @TableField("hospital_id")
    private String hospitalId;

    /**
     * 医院logo
     */
    @TableField("hospital_logo")
    private String hospitalLogo;

    /**
     * 指导单别名
     */
    @TableField("guidance_alias_name")
    private String guidanceAliasName;

    /**
     * 用户二维码场景id
     */
    @TableField("user_qrcode_scene_id")
    private String userQrcodeSceneId;

    /**
     * 用户二维码正文
     */
    @TableField("user_qrcode_content")
    private String userQrcodeContent;

    /**
     * 用户二维码FTP文件路径
     */
    @TableField("user_qrcode_ftp_path")
    private String userQrcodeFtpPath;

    /**
     * 用户二维码创建时间
     */
    @TableField("user_qrcode_create_time")
    private LocalDateTime userQrcodeCreateTime;

    /**
     * 用户二维码失效时间
     */
    @TableField("user_qrcode_invalid_time")
    private LocalDateTime userQrcodeInvalidTime;

    /**
     * 二维码原始编码
     */
    @TableField("qrcode_init_code")
    private String qrcodeInitCode;

    /**
     * 保存时间
     */
    @TableField("save_time")
    private LocalDateTime saveTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @TableField("updater")
    private String updater;

    /**
     * 是否删除  0：否  1：是
     */
    @TableField("delete_flag")
    private Boolean deleteFlag;


}
