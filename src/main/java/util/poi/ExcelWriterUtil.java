package util.poi;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 18435 on 2018/3/22.
 * 向excel表格中写入数据
 */
public class ExcelWriterUtil {
    public static String objectiveFilePath = "/resources/logExcel/";
    private static Logger logger = LoggerFactory.getLogger(ExcelWriterUtil.class);

    /**
     * 写之前excel文件已存在
     *
     * @param dataList
     * @param finalXlsxFile
     */
    public static void readAfterWriteExcel(List<Map> dataList, File finalXlsxFile){
        OutputStream out = null;
        // 读取Excel文档
        Workbook workBook = ExcelExtractUtil.getWorkbook(finalXlsxFile.getName(),finalXlsxFile);
        // sheet 对应一个工作页
        Sheet sheet = workBook.getSheetAt(0);
        //获取总行数
        int rowNumber = sheet.getLastRowNum();  // 第一行从0开始算
        logger.info("原始数据总行数，除属性行：" + rowNumber);
        //获取总列数
        int coloumNum = sheet.getRow(0).getPhysicalNumberOfCells();
        logger.info("原始数据总列数，除属性行：" + coloumNum);
        /**
         * 往Excel中写新数据
         */
        Row headRow = sheet.getRow(0);
        for (int j = 0; j < dataList.size(); j++) {
            // 创建一行：从第二行开始，跳过属性列
            Row row = sheet.createRow(rowNumber + j + 1);
            // 得到要插入的每一条记录
            Map dataMap = dataList.get(j);

            for (int k = 0; k < coloumNum; k++) {
                // 在一行内循环
                String headValue = ExcelExtractUtil.getCellValue(headRow.getCell(k));
                row.createCell(k).setCellValue(dataMap.get(headValue).toString());
            }
        }
        // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        outputExcel(finalXlsxFile, workBook);
        logger.info("数据导出成功");
    }

    /**
     * 生成Excel文件存储路径
     * @return
     */
    private static String getExcelFilePath() {
        String relativelyPath = System.getProperty("user.dir");
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = sfm.format(new Date()) + "数据.xls";
        return relativelyPath + ExcelWriterUtil.objectiveFilePath + fileName;
    }

    public static void readAndWriterExcel(List<Map> list) {
        String finalXlsxPath = getExcelFilePath(); //获取Excel路径
        // 读取Excel文档
        File finalXlsxFile = new File(finalXlsxPath);
        if (finalXlsxFile.exists()) {//文件已存在 先读再写
            readAfterWriteExcel(list, finalXlsxFile);
        } else {//文件不存在，直接写数据
            writerExcel(list,finalXlsxFile);
        }
    }

    /**
     * 先创建文件，再向文件中写入数据
     *
     * @param dataList
     * @param file
     */
    public static void writerExcel(List<Map> dataList, File file){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("记录表");
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //处理第一行，表头
        HSSFRow row = sheet.createRow(0);
        Set<String> headSet = dataList.get(0).keySet();
        int headIndex = 0;
        for(String headValue : headSet){
            row.createCell(headIndex++).setCellValue(headValue);
        }

        //填充数据
        for (int j = 0; j < dataList.size(); j++) {
            // 创建数据行：从第二行开始，跳过属性列
            Row rowData = sheet.createRow(j + 1);
            // 得到要插入的每一条记录
            Map dataMap = dataList.get(j);
            int k = 0;
            for(String value : headSet){
                rowData.createCell(k++).setCellValue(dataMap.get(value).toString());
            }
        }
        // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        outputExcel(file, wb);
        logger.info("数据导出成功");
    }

    /**
     * 输入excel文件
     *
     * @param file
     * @param wb
     */
    private static void outputExcel(File file, Workbook wb) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            wb.write(fos);
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        List<Map> list = new ArrayList<Map>();
        String[] column = {"BankName", "Addr", "Phone"};
        Map<String,String> map = new HashMap<String,String>();
        map.put("BankName","中国银行");
        map.put("Addr","");
        map.put("Phone","13467234412");

        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("BankName","中国银行测试2");
        map1.put("Addr","望京街道测试2");
        map1.put("Phone","");

        list.add(map);
        list.add(map1);

        ExcelWriterUtil.readAndWriterExcel(list);
    }
}
