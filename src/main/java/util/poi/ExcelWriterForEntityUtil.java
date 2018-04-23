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
import util.poi.exception.PoiExtractException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 18435 on 2018/3/23.
 */
public class ExcelWriterForEntityUtil {
    public static String objectiveFilePath = "/resources/logExcel/";
    private static Logger logger = LoggerFactory.getLogger(ExcelWriterForEntityUtil.class);

    /**
     * 写之前excel文件已存在
     *
     * @param dataList
     * @param finalXlsxFile
     */
    public static void readAfterWriteExcel(List<Object> dataList, Class clazz, File finalXlsxFile) {
        OutputStream out = null;
        // 读取Excel文档
        Workbook workBook = ExcelExtractUtil.getWorkbook(finalXlsxFile.getName(), finalXlsxFile);
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
            // 得到要插入的每一条记录  获取对象
            Object data = dataList.get(j);

            for (int k = 0; k < coloumNum; k++) {
                // 在一行内循环
                String headValue = ExcelExtractUtil.getCellValue(headRow.getCell(k));
                try {
                    Field field = clazz.getDeclaredField(headValue);
                    field.setAccessible(true);
                    String cellValue = field.get(data)==null ? "" :field.get(data).toString();
                    row.createCell(k).setCellValue(cellValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        outputExcel(finalXlsxFile, workBook);
        logger.info("数据导出成功");
    }

    /**
     * 生成Excel文件存储路径
     *
     * @return
     */
    private static String getExcelFilePath() {
        String relativelyPath = System.getProperty("user.dir");
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = sfm.format(new Date()) + "数据.xls";
        return relativelyPath + ExcelWriterUtil.objectiveFilePath + fileName;
    }

    public static void readAndWriterExcel(List<Object> list, Class clazz) {
        String finalXlsxPath = getExcelFilePath(); //获取Excel路径
        // 读取Excel文档
        File finalXlsxFile = new File(finalXlsxPath);
        if (finalXlsxFile.exists()) {//文件已存在 先读再写
            readAfterWriteExcel(list, clazz, finalXlsxFile);
        } else {//文件不存在，直接写数据
            writerExcel(list, clazz, finalXlsxFile);
        }
    }

    /**
     * 先创建文件，再向文件中写入数据
     *
     * @param dataList
     * @param file
     */
    public static void writerExcel(List<Object> dataList, Class clazz, File file) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("记录表");
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //处理第一行，表头
        HSSFRow row = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            row.createCell(i).setCellValue(fields[i].getName());
        }

        //填充数据
        for (int j = 0; j < dataList.size(); j++) {
            // 创建数据行：从第二行开始，跳过属性列
            Row rowData = sheet.createRow(j + 1);
            Object data = dataList.get(j);
            if(!data.getClass().getName().equals(clazz.getName())){//如果类型不一致，抛出异常
                throw new PoiExtractException("数据格式化类型不一致");
            }
            //得到要插入的每一条记录  需要根据反射获取属性值的问题
            for (int i = 0; i < fields.length; i++) {
                String headValue = ExcelExtractUtil.getCellValue(row.getCell(i));
                try {
                    Field field = clazz.getDeclaredField(headValue);
                    field.setAccessible(true);
                    String cellValue = field.get(data)==null ? "" :field.get(data).toString();
                    rowData.createCell(i).setCellValue(cellValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
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
        } catch (IOException e) {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();

        List<String> imgURL = new ArrayList<>();
        imgURL.add("urlOne");
        imgURL.add("urlTwo");
        Person person = new Person("张三", 12, "男",date.getTime());
        Person person1 = new Person( 15, "女",date.getTime());
        Person person2 = new Person("穆庭轩", date.getTime());
        person1.setImgURl(imgURL);
        person2.setImgURl(imgURL);
        List<Object> list = new ArrayList<>();
        list.add(person);
        list.add(person1);
        list.add(person2);
        ExcelWriterForEntityUtil.readAndWriterExcel(list, Person.class);
    }
}
