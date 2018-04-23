package util.poi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 18435 on 2018/3/23.
 */
public class ExcelWriterForJson {
    public static String objectiveFilePath = "/resources/logExcel/";
    private static Logger logger = LoggerFactory.getLogger(ExcelWriterForJson.class);
    public static String relativelyPath = System.getProperty("user.dir");


    /**
     * 向excel文件中写入数据   自动生成文件名
     * @param list
     */
    public static void readAndWriterExcel(List<JSONObject> list){
        String finalXlsxPath = getExcelFilePath(); //获取Excel路径
        // 读取Excel文档
        File finalXlsxFile = new File(finalXlsxPath);
        writerFile(list,finalXlsxFile);
    }

    /**
     * 向excel文件中写入数据 自己规定文件名
     * @param list
     * @param fileName
     */
    public static void readAndWriterExcel(List<JSONObject> list,String fileName){
        // 读取Excel文档
        File finalXlsxFile = new File(relativelyPath + ExcelWriterForJson.objectiveFilePath + fileName);
        writerFile(list,finalXlsxFile);
    }

    /**
     *  向文件中写数据
     * @param list
     * @param file
     */
    private static void writerFile(List<JSONObject> list,File file){
        createDirectory(file);
        if (file.exists()) {//文件已存在 先读再写
            readAfterWriteExcel(list, file);
        } else {//文件不存在，直接写数据
            writerExcel(list, file);
        }
    }

    /**
     * 写之前excel文件已存在
     *
     * @param list
     * @param finalXlsxFile
     */
    public static void readAfterWriteExcel(List<JSONObject> list, File finalXlsxFile) {
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
        for (int j = 0; j < list.size(); j++) {
            // 创建一行：从第二行开始，跳过属性列
            Row row = sheet.createRow(rowNumber + j + 1);
            // 得到要插入的每一条记录
            JSONObject jsonObject = list.get(j);
            for (int k = 0; k < coloumNum; k++) {
                // 在一行内循环
                String headValue = ExcelExtractUtil.getCellValue(headRow.getCell(k));
                String value = getJsonValue(jsonObject.get(headValue));
                row.createCell(k).setCellValue(value);
            }
        }
        // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        outputExcel(finalXlsxFile, workBook);
        logger.info("数据导出成功");
    }


    /**
     * 先创建文件，再向文件中写入数据
     *
     * @param list
     * @param file
     */
    public static void writerExcel(List<JSONObject> list, File file) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("记录表");
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //处理第一行，表头
        HSSFRow row = sheet.createRow(0);
        int headIndex = 0;
        for (Map.Entry<String, Object> entry : list.get(0).entrySet()) {
            row.createCell(headIndex++).setCellValue(entry.getKey());
        }

        //填充数据
        for (int j = 0; j < list.size(); j++) {
            // 创建数据行：从第二行开始，跳过属性列
            Row rowData = sheet.createRow(j + 1);
            // 得到要插入的每一条记录
            JSONObject jsonObject = list.get(j);
            for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                String headValue = ExcelExtractUtil.getCellValue(row.getCell(k));
                String value = getJsonValue(jsonObject.get(headValue));
                rowData.createCell(k).setCellValue(value);
            }
        }
        // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
        outputExcel(file, wb);
        logger.info("数据导出成功");
    }


    /**
     * 生成Excel文件存储路径
     *
     * @return
     */
    private static String getExcelFilePath() {
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = sfm.format(new Date()) + "数据.xls";
        return relativelyPath + ExcelWriterForJson.objectiveFilePath + fileName;
    }

    /**
     * 创建目录
     * @param file
     * @return
     */
    private static boolean createDirectory(File file){
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            logger.info("目标文件所在目录不存在，准备创建它！");
            if(!file.getParentFile().mkdirs()) {
                logger.info("创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }



    /**
     * 写入excel文件
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

    /**
     * 获取json中不同格式的值  主要是对JSONArray的处理
     * @param object
     * @return
     */
    private static String getJsonValue(Object object) {
        String value = object.toString();
        if (object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            StringBuffer sb = new StringBuffer();
            for (int x = 0; x < jsonArray.size(); x++) {
                sb.append(jsonArray.get(x) + ",");
            }
            value = sb.toString();
        }
        return value;
    }

    public static void main(String[] args){
        Person person1 = new Person("里斯", 15, "女",new Date().getTime());
        String jsonStr = "{\"name\":\"测试\",\"address\":\"北京市朝阳区\",\"age\":2,\"img\":['路径1','路径2','路径3']}";
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        System.out.println(jsonObject);
        List<JSONObject> list =  new ArrayList<>();
        list.add(jsonObject);
        ExcelWriterForJson.readAndWriterExcel(list);

    }
}
