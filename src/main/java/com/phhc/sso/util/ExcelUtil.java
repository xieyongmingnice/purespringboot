package com.phhc.sso.util;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.annotation.ExcelField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    @ExcelField(title = "demo", order = 1)
    private String demo;

    public static List importExcel(MultipartFile file, Object object) {
        long start = System.currentTimeMillis();
        List list = new ArrayList();
        try {
            list = ExcelUtils.getInstance().readExcel2Objects(file.getInputStream(),
                    Object.class);
        } catch (Exception e) {
            logger.error("数据导入异常！！,{},{}",e.getMessage(),e);
        }
        return list;
    }

    public void exportExcel(HttpServletResponse response, List list, Object o, String excelName) {
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list, Object.class, true, excelName, true, response.getOutputStream());
        }catch (Exception e){
            logger.error("数据导出异常！！,{},{}",e.getMessage(),e);
        }
    }
}
