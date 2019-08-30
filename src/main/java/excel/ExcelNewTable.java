package excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

/**
 * @Description excel 转建表 sql
 * @Author liangchen
 * @Date 2019/8/30 17:48
 * @Param
 * @return
 **/
public class ExcelNewTable {
    public static void main(String[] args) {
        String filePath = "C:/Users/dell/Desktop/劳务系统建表.xlsx";
        String newFilePath = "C:/Users/dell/Desktop/劳务系统建表.sql";

        File file = new File(newFilePath);
        if (file.exists()) {
            file.delete();
        }
        String suffix = filePath.substring(filePath.lastIndexOf("."));
            readExcel(filePath, newFilePath);
    }

    public static void readExcel(String filePath, String newFilePath) {

        try {
            //获取工作簿
            Workbook workbook = WorkbookFactory.create(new FileInputStream(filePath));
            //获取工作簿的工作表数量
            int sheetsNum = workbook.getNumberOfSheets();
            //遍历读取每个工作表
            for (int i = 0; i < sheetsNum; i++) {
                //获取工作表注释
                String tableComment = workbook.getSheetName(i);
                //获取工作表
                Sheet sheet = workbook.getSheet(tableComment);
                //获取表名
                String sheetName = sheet.getRow(1).getCell(4).toString();
                //获取最后一行
                int lastRowNum = sheet.getLastRowNum();
                //建表语句开头
                String tableHead = "CREATE TABLE " + sheetName + "(";
                String tableEnd = ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT=" + "'" + tableComment + "';";

                writeTxt("##################################", newFilePath);
                writeTxt("##################################", newFilePath);
                writeTxt("#" + tableComment, newFilePath);
                writeTxt("##################################", newFilePath);
                //写入头
                writeTxt(tableHead, newFilePath);
                //写入id
                writeTxt("      id bigint(20) NOT NULL comment '主键id',", newFilePath);
                //遍历写入excel字段
                for (Row row : sheet) {
                    int curRowNum = row.getRowNum();
                    if (row == sheet.getRow(0)) {
                        continue;
                    }
                    Cell filedName = row.getCell(0);
                    Cell dataType = row.getCell(1);
                    Cell required = row.getCell(2);
                    Cell annotate = row.getCell(3);
                    String req;
                    if (required.toString().equals("是")) {
                        req = "NOT NULL";
                    } else {
                        req = "NULL";
                    }
                    if (StringUtils.isBlank(filedName.toString())) {
                        continue;
                    }

                    String content = trimStr(filedName.toString()) + " " + trimStr(dataType.toString()) + " " + req + " comment '" + annotate + "',";
                    writeTxt("      " + content, newFilePath);
                }
                //写入固定字段
                writeTxt("      requestcode varchar(64) DEFAULT NULL COMMENT '请求序列编码',", newFilePath);
                writeTxt("      create_by varchar(64) DEFAULT NULL COMMENT '创建人',", newFilePath);
                writeTxt("      create_time datetime DEFAULT NULL COMMENT '创建时间',", newFilePath);
                writeTxt("      update_by varchar(64) DEFAULT NULL COMMENT '更新人',", newFilePath);
                writeTxt("      update_time datetime DEFAULT NULL COMMENT '更新时间',", newFilePath);
                writeTxt("      remarks varchar(255) DEFAULT NULL COMMENT '备注',", newFilePath);
                writeTxt("      del_flag char(1) DEFAULT NULL COMMENT '删除标记（0正常、1删除）',", newFilePath);
                //写入主键
                writeTxt("PRIMARY KEY(id)", newFilePath);
                //写入结尾
                writeTxt(tableEnd, newFilePath);
                writeTxt("##################################", newFilePath);
                writeTxt("", newFilePath);
                writeTxt("", newFilePath);
                System.out.println("成功生成：" + tableComment);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

    }

    //写入目标文件
    public static void writeTxt(String content, String newFileParh) {
        try {
            //如果文件不存在，新建
            File file = new File(newFileParh);
            if (!file.exists()) {
                file.createNewFile();
            }
            //获取文件写入对象，并指定可append
            FileWriter fw = new FileWriter(newFileParh, true);
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

    /**
     * @return void
     * @Description 去除空格（中文全角空格和半角空格）
     * @Author liangchen
     * @Date 2019/8/30 9:55
     * @Param [str]
     **/
    public static String trimStr(String str) {
        str = str.replace((char) 12288, ' ');
        str = str.trim();
        return str;
    }
}
