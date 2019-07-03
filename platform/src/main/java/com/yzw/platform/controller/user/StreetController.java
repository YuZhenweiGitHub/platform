package com.yzw.platform.controller.user;

import com.yzw.platform.entity.user.InfoStreet;
import com.yzw.platform.entity.user.VillageQk;
import com.yzw.platform.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/street")
public class StreetController {

    @Autowired
    private UserService userService;

    @RequestMapping("/uploadExcel.json")
    public @ResponseBody
    Object uploadExcel1(@RequestParam MultipartFile streetFile) {
        if(!streetFile.isEmpty()){
            String fileName = streetFile.getOriginalFilename();
            try {
                InputStream is = streetFile.getInputStream();
                Workbook hssfWorkbook = null;
                if (fileName.endsWith("xlsx")){
                    hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
                }else if (fileName.endsWith("xls")){
                    hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
                } else {
                    return "fail";
                }
                List<String> error = new ArrayList<>();
                // 循环工作表Sheet
                for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
                    Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                    if (hssfSheet == null) {
                        continue;
                    }
                    log.info(">>>>>>>>>>{}街道数据查找开始",hssfSheet.getSheetName());
                    // 循环行Row
                    for (int rowNum = 0 ; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                        Row hssfRow = hssfSheet.getRow(rowNum);
                        if (hssfRow != null) {
                            String villageId = hssfRow.getCell(8).getStringCellValue().trim();
                            String prcId = hssfRow.getCell(7).getStringCellValue().trim();
                            String jiedaoName = hssfRow.getCell(6).getStringCellValue().trim();
                            String prcName = hssfRow.getCell(5).getStringCellValue().trim();
                            List<InfoStreet> iss = userService.selectInfoStreet(jiedaoName);
                            if (null == iss || iss.size() == 0 ) {
                                error.add(jiedaoName);
                                log.info(">>>>>>>>>>街道数据:{},数据库中不存在",jiedaoName);
                            } else {
                                Integer streetId = iss.get(0).getId();
                                String streetName = iss.get(0).getName().trim();
                                writeFile(MessageFormat.format("update info_village set prc_id = {0}, prc_name={1},street_id = {2} , street_name = {3} where id = {4};", prcId,"'"+prcName+"'",streetId, "'"+streetName+"'", villageId));
                            }
                        }
                    }
                    log.info(">>>>>>>>>>{}新街道数据录入结束，有效数据行数：{}",hssfSheet.getSheetName(),hssfSheet.getLastRowNum());
                }
                error.stream().forEach(s -> System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}"+s));
            } catch (Exception e) {
                log.info(">>>>>>>>>>数据导入异常:{}",e);
                return "error";
            }
        }
        return "ok";
    }

   @RequestMapping("/uploadExcel1.json")
   public @ResponseBody
   Object uploadExcel(@RequestParam MultipartFile streetFile) {
       if(!streetFile.isEmpty()){
           String fileName = streetFile.getOriginalFilename();
           try {
               InputStream is = streetFile.getInputStream();
               Workbook hssfWorkbook = null;
               if (fileName.endsWith("xlsx")){
                   hssfWorkbook = new XSSFWorkbook(is);//Excel 2007
               }else if (fileName.endsWith("xls")){
                   hssfWorkbook = new HSSFWorkbook(is);//Excel 2003
               } else {
                   return "fail";
               }
               List<String> address = new ArrayList<>();
               // 循环工作表Sheet
               for (int numSheet = 0; numSheet <hssfWorkbook.getNumberOfSheets(); numSheet++) {
                   Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                   if (hssfSheet == null) {
                       continue;
                   }
                   log.info(">>>>>>>>>>{}小区数据查找开始",hssfSheet.getSheetName());
                   // 循环行Row
                   for (int rowNum = 1 ; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                       Row hssfRow = hssfSheet.getRow(rowNum);
                       if (hssfRow != null) {
                           String roomAddress = hssfRow.getCell(4).toString();
                           address.add("'"+roomAddress+"'");
                       }
                   }
               }
               writeFile("("+StringUtils.join(address.iterator(),",")+")");
           } catch (Exception e) {
               log.info(">>>>>>>>>>数据导入异常:{}",e);
               return "error";
           }
       }
       return "ok";
   }
    public boolean writeFile( String context) throws Exception {
        Boolean flag = false;
        String str = context +"\r\n";
        String temp = "";
        FileInputStream fileInputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;

        try {
            //生成文件路径
            String filePath = "D:\\file\\sql.txt";
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            isr = new InputStreamReader(fileInputStream);
            br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            for (int i = 0;(temp = br.readLine())!=null; i++) {
                sb.append(temp);
                sb = sb.append(System.getProperty("line.separator"));
            }
            sb.append(str);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(sb.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
        }
        return flag;
    }

}
