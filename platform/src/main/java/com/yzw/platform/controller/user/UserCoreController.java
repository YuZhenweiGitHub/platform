package com.yzw.platform.controller.user;

import com.yzw.platform.core.TokenManager;
import com.yzw.platform.dto.user.UserQueryDto;
import com.yzw.platform.entity.user.InfoStreet;
import com.yzw.platform.entity.user.UserInfo;
import com.yzw.platform.enums.ResultMessageEnum;
import com.yzw.platform.service.user.UserService;
import com.yzw.platform.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.Line;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserCoreController {

    @Autowired
    private UserService userService;

    /**
     * 退出系统
     * @return
     */
    @RequestMapping(value = "loginOut.html", method = RequestMethod.GET)
    public String loginOut () {
        TokenManager.loginOut();
        return ConstantPath.REDIRECT_LOGIN_URL;
    }

    @RequestMapping(value = "/reset.json",method = RequestMethod.POST)
    public @ResponseBody
    Object reset (HttpServletRequest request, @Validated @RequestBody UserQueryDto queryDto) {
        UserInfo userInfo = (UserInfo) SessionUtils.getAttribute(request, Constant.PLATFORM_USER_SESSION_KEY);
        if (null == userInfo) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.ILLEGAL_REQUEST_ERROR);
        }
        String newPassWord = MD5Util.createMD5(queryDto.getNewPassWord());
        if (userInfo.getPassWord().equals(newPassWord)) {
            return ResultUtils.buildMessageByEnum(ResultMessageEnum.NEW_OLD_PWD_ERROR);
        }
        userInfo.setPassWord(newPassWord);
        // 更新用户密码字段
        userService.updateUserInfo(userInfo);
        // 更新session用户信息
        SessionUtils.setAttribute(request, Constant.PLATFORM_USER_SESSION_KEY, userInfo);
        return ResultUtils.buildSuccessMessage();
    }

    @RequestMapping("/uploadExcel.json")
    public @ResponseBody Object uploadExcel(@RequestParam MultipartFile streetFile, HttpServletRequest request) {
        if(!streetFile.isEmpty()){
            String fileName = streetFile.getOriginalFilename();
            try {
                //this.readExcel(fileName,streetFile);
                this.readExcelByNewStreet(fileName,streetFile);
                //this.updateCode(fileName,streetFile);
            } catch (Exception e) {
                log.info(">>>>>>>>>>数据导入异常:{}",e);
                return "error";
            }
        }
        return "ok";
    }

    /**
     * 读取房源streetExcel表数据
     * @param fileName
     * @param streetFile
     * @throws Exception
     */
    private void readExcel(String fileName,MultipartFile streetFile) throws Exception{
        InputStream is = streetFile.getInputStream();
        Workbook hssfWorkbook = null;
        if (fileName.endsWith("xlsx")){
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        }else if (fileName.endsWith("xls")){
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
        } else {
            return;
        }
        InfoStreet street = null;
        List<InfoStreet> streetList = new ArrayList<>();
        Map<String, InfoStreet> map  = new HashMap<>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
            //HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);-
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            log.info(">>>>>>>>>>{}数据录入开始",hssfSheet.getSheetName());
            // 循环行Row
            for (int rowNum = 1 ; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    street = new InfoStreet();
                    Cell id = hssfRow.getCell(0);
                    String name = hssfRow.getCell(3).toString();
                    Cell createTime = hssfRow.getCell(5);
                    Cell ts = hssfRow.getCell(11);
                    street.setId(new Double(id.getNumericCellValue()).intValue());
                    street.setAreaId(0);
                    street.setCode("0");
                    street.setName(name);
                    street.setCreateTime(createTime.getDateCellValue());
                    street.setDistrictGbCode("0");
                    street.setTs(ts.getDateCellValue().getTime());
                    if (map.containsKey(name) || name.indexOf("(") > -1 || name.indexOf("（") > -1) {
                        // 重复数据标记为删除
                        street.setStatus(1);
                    } else {
                        street.setStatus(0);
                        map.put(name, street);
                    }
                    streetList.add(street);
                }
            }
            log.info(">>>>>>>>>>{}数据录入结束，有效数据行数：{}",hssfSheet.getSheetName(),hssfSheet.getLastRowNum());
        }
        log.info(">>>>>>>>>>{}街道数据录入结束，录入总行数：{}",fileName,streetList.size());
        int dataSize = streetList.size();
        if (dataSize == 0 ) {
            return;
        }
        int pageSize = 100;
        if (dataSize > pageSize) {
            int totalPageNum = dataSize / pageSize;
            if(0 != dataSize % pageSize){
                totalPageNum++;
            }
            for (int i = 1; i <= totalPageNum; i++) {
                userService.insertForeachStreet(this.getPagedList(streetList, i, pageSize));
            }
        } else {
            userService.insertForeachStreet(streetList);
        }
    }

    public List<InfoStreet> getPagedList(List<InfoStreet> data,int pageNum,int pageSize) {
        int fromIndex = (pageNum - 1) * pageSize;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();
        }
        int toIndex = pageNum * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }

    /**
     * 读取并更新国家统计局街道code和行政区code
     * @param fileName
     * @param streetFile
     * @throws Exception
     */
    private void updateCode(String fileName,MultipartFile streetFile) throws Exception{
        InputStream is = streetFile.getInputStream();
        Workbook hssfWorkbook = null;
        if (fileName.endsWith("xlsx")){
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        }else if (fileName.endsWith("xls")){
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
        } else {
            return;
        }
        List<InfoStreet> streetList = new ArrayList<>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
            //HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);-
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            log.info(">>>>>>>>>>{}数据更新开始",hssfSheet.getSheetName());
            // 循环行Row
            for (int rowNum = 1 ; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    Cell code = hssfRow.getCell(3);
                    Cell name = hssfRow.getCell(1);
                    Cell prcCode = hssfRow.getCell(2);
                    int pCode = new Double(prcCode.getStringCellValue()).intValue();
                    String jCode = pCode +""+ code.getStringCellValue();
                    List<InfoStreet> sts = userService.selectInfoStreet(name.toString());
                    sts.stream().forEach(infoStreet -> {
                        infoStreet.setDistrictGbCode(pCode +"000000");
                        if (jCode.indexOf(".") > -1) {
                            infoStreet.setCode(jCode.split(".")[0]);
                        } else {
                            infoStreet.setCode(jCode);
                        }
                        userService.updateStreet(infoStreet);
                        streetList.add(infoStreet);
                    });
                }
            }
            log.info(">>>>>>>>>>街道code更新结束，更新行数：{}",streetList.size());
        }
    }

    private void readExcelByNewStreet(String fileName,MultipartFile streetFile) throws Exception{
        InputStream is = streetFile.getInputStream();
        Workbook hssfWorkbook = null;
        if (fileName.endsWith("xlsx")){
            hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
        }else if (fileName.endsWith("xls")){
            hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
        } else {
            return;
        }
        InfoStreet street = null;
        List<InfoStreet> streetList = new ArrayList<>();
        Map<String, InfoStreet> map  = new HashMap<>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
            //HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);-
            Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            log.info(">>>>>>>>>>{}新街道数据录入开始",hssfSheet.getSheetName());
            // 循环行Row
            for (int rowNum = 1 ; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                //HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    street = new InfoStreet();
                    String jiedaoName = hssfRow.getCell(0).toString();
                    String prcCode = hssfRow.getCell(2).getStringCellValue();
                    String jiedaoCode = hssfRow.getCell(3).getStringCellValue();
                    int pCode = new Double(prcCode).intValue();
                    if (map.containsKey(jiedaoName)) {
                        log.info(">>>>>>>>>>新街道数据:{},已新增",jiedaoName);
                        continue;
                    }
                    List<InfoStreet> iss = userService.selectInfoStreet(jiedaoName);
                    if (iss.size() > 0 ) {
                        log.info(">>>>>>>>>>新街道数据:{},已存在无需录入",jiedaoName);
                    } else {
                        street.setCode(pCode+jiedaoCode);
                        street.setName(jiedaoName);
                        street.setStatus(0);
                        street.setCreateTime(new Date());
                        street.setDistrictGbCode(pCode +"000000");
                        street.setTs(System.currentTimeMillis());
                        street.setAreaId(0);
                        map.put(jiedaoName, street);
                        streetList.add(street);
                    }
                }
            }
            log.info(">>>>>>>>>>{}新街道数据录入结束，有效数据行数：{}",hssfSheet.getSheetName(),hssfSheet.getLastRowNum());
        }
        log.info(">>>>>>>>>>{}新街道数据录入结束，录入总行数：{}",fileName,streetList.size());
        int dataSize = streetList.size();
        if (dataSize == 0) {
            return;
        }
        int pageSize = 100;
        if (dataSize > pageSize) {
            int totalPageNum = dataSize / pageSize;
            if(0 != dataSize % pageSize){
                totalPageNum++;
            }
            for (int i = 1; i <= totalPageNum; i++) {
                userService.insertForeachStreet(this.getPagedList(streetList, i, pageSize));
            }
        } else {
            userService.insertForeachStreet(streetList);
        }
    }
}
