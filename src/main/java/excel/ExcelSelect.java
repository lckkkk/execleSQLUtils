package excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取excel查询语句
 * 使用参数：
 * newFilePath：生成sql文件路径
 * filePath：读取的excel文件路径
 */
public class ExcelSelect {

    public static void main(String[] args) {
        String newFilePath =  "C:/Users/dell/Desktop/查询.sql";
        String filePath = "C:/Users/dell/Desktop/天筑/信通/04.02 信通 权限数据/权限2.xls";
        //调用方法在控制台打印查询语句
        getSelectByExcel(newFilePath, filePath);

    }

    //在控制台打印查询语句
    public static void getSelectByExcel(String newFilePath, String filePath) {
        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(filePath));
            Integer sheetNums = hssfWorkbook.getNumberOfSheets();
            String content = null;
            String permission = null;
            StringBuffer permissionBuffer = new StringBuffer();
            List<String> sheetNames = new ArrayList<String>();
            for (int i = 0; i < sheetNums; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                sheetNames.add(sheet.getSheetName());
                int lastRowNum = sheet.getLastRowNum();
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    int rowNum = row.getRowNum();
                    String name = row.getCell(0).toString();
                    if (rowNum == lastRowNum && i == sheetNums - 1) {
                        permission = row.getCell(1).toString();
                    } else {
                        permission = row.getCell(1).toString() + "' or permission = '";
                    }
                    permissionBuffer = permissionBuffer.append(permission);
                }
            }
            content = getSelect(permissionBuffer.toString());
            System.out.println(content);
            writeTxt(content, newFilePath);

            //打印读取的sheet
            System.out.println();
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

    //生成select 语句
    public static String getSelect(String permission) {
        String selectStart = "select * from sys_permission where permission = '";
        String selectEnd = "';";

        String selectContent = selectStart + permission + selectEnd;

        return selectContent;
    }
}
