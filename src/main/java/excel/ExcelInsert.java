package excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取excel插入数据库
 * 使用参数：
 * newFilePath：生成sql文件
 * orgId:插入权限的组织id
 * filePath：读取的excel文件
 */
public class ExcelInsert {

    public static void main(String[] args) {
        String newFilePath = "C:/Users/dell/Desktop/插入.sql";
        String orgId = "3342b5c9c5a44fa6b22e82fa4412242f";
        String filePath = "C:/Users/dell/Desktop/天筑/信通/04.02 信通 权限数据/权限.xls";

        File file = new File(newFilePath);
        if (file.exists()) {
            file.delete();
        }
        getInsertByExcel(newFilePath, filePath, orgId);
    }

    //读取excel中的数据并写入sql文件
    public static void getInsertByExcel(String newFilePath, String filePath, String orgId) {
        try {
            //指定读取的xls文件。
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(filePath));
            int sheetNums = hssfWorkbook.getNumberOfSheets();
            List<String> sheetNames = new ArrayList<String>();
            //读取每一个sheet
            for (int i = 0; i < sheetNums; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                //获取sheet的总数
                int numberOfSheets = hssfWorkbook.getNumberOfSheets();
                sheetNames.add(sheet.getSheetName());
                //读取每一行
                for (Row row : sheet) {
                    //第一行不读（表头）
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    String name = row.getCell(0).getStringCellValue();
                    String permission = row.getCell(1).getStringCellValue();
                    //获取写入内容
                    String content1 = insertSysContent(name, permission);
                    String content2 = insertOrgContent(name, permission, orgId);
                    //写入
                    writeTxt(content1, newFilePath);
                    writeTxt(content2, newFilePath);
                }
            }
            //打印读取的sheet
            System.out.println("读取的sheet：");
            System.out.println("------------------------------------------------------------");
            for (String sheetName : sheetNames) {
                System.out.println(sheetName);
            }
            System.out.println("------------------------------------------------------------");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //写入目标文件
    public static void writeTxt(String content, String newFilePath) {
        try {
            //如果文件不存在，新建
            File file = new File(newFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            //获取文件写入对象，并指定可append
            FileWriter fw = new FileWriter(newFilePath, true);
            //获取写入缓冲区对象
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            //每写一次换行
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //生成sys insert 语句
    public static String insertSysContent(String name, String permission) {
        String insertStart = "insert into sys_permission (uid, name, sort, permission, create_by, update_by,type, del_flag, create_time, update_time) values (MD5(UUID()),'";
        String insertCenter1 = "', 10,'";
        String insertSysFin = "', 1, 1, 6, 0,NOW(), NOW());";
        String sysContent = insertStart + name + insertCenter1 + permission + insertSysFin;

        return sysContent;
    }

    //生成org insert 语句
    public static String insertOrgContent(String name, String permission, String orgId) {
        String insertStart = "insert into org_permission (uid, name, sort, permission, create_by, update_by,type, del_flag, create_time, update_time, org_id) values (MD5(UUID()),'";
        String insertCenter1 = "', 10,'";
        String insertEnd = "', 1, 1, 6, 0,NOW(), NOW(),'" + orgId + "');";

        String orgContent = insertStart + name + insertCenter1 + permission + insertEnd;

        return orgContent;
    }
}
