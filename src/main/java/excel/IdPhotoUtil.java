package excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import utils.MD5Utils;
import utils.RandomUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 检测协会 证件照批量上传
 * 使用参数：
 */
public class IdPhotoUtil {

    public static void main(String[] args) {
        String photoFilePath = "C:/Users/dell/Desktop/老系统数据/allphoto";
        String qrFilePath = "C:/Users/dell/Desktop/老系统数据/allQrCode";
        uploadPhoto(photoFilePath);

    }

    //上传证件照
    public static void uploadPhoto(String photoFilePath) {
        //获取证件照文件夹
        File allPhoto = new File(photoFilePath);
        //获取文件夹下所有文件
        File[] photos = allPhoto.listFiles();
        for (File photo : photos) {
            String photoName = photo.getName();
            String[] nameAndCode = getNameAndCode(photoName);
            String uuid = RandomUtils.getUUID();

            String name = "'" + nameAndCode[0] + getSuffix(photoName) + "'";
            String md5Str = "'" + MD5Utils.md5(photo, false) + "'";
            String suffix = "'" + getSuffix(photoName) + "'";
            String fileSize = "'" + (photo.length() / 1024) + "kb'";
            String refId = "'" + nameAndCode[1].substring(0, 12) + "'";
            String category = "'检测卡证件照'";
            String storeUrl = "'./attachment/get/" + uuid + "'";
            String storePath = "'C:/Users/dell/Desktop/老系统数据/allphoto/" + photoName + "'";
//            String storePath = "'/home/attachment/checkCardPhoto/" + photoName + "'";
            String guid = "'" + uuid + "'";
            String createDate = "'20190626'";
            String createTime = "'104222'";
            System.out.println();
//            System.out.println(name + " - " + md5Str + " - " + suffix + " - " + fileSize + " - " + refId + " - " + category + " - " + storeUrl + " - " + storePath + " - " + guid + " - " + createDate + " - " + createTime);

            String insertPhoto = "insert into jcxh_attachment (name, md5Str, suffix, fileSize, refId, category, storeUrl, storePath, guid, createDate, createTime) values ("+
                    name+","+md5Str+","+suffix+","+fileSize+","+refId+","+category+","+storeUrl+","+storePath+","+guid+","+createDate+","+createTime+");";

            System.out.println(insertPhoto);

            ExcelInsert.writeTxt(insertPhoto,"C:/Users/dell/Desktop/insertPhoto测试库.sql");
        }
    }

    //截取获取姓名和检测卡编码
    public static String[] getNameAndCode(String fileName) {
        String[] s = fileName.split("_");
        return s;
    }

    //获取文件后缀
    public static String getSuffix(String fileName) {
        String[] s = fileName.split("\\.");
        return "." + s[1];
    }

    //生成sys insert 语句

}
