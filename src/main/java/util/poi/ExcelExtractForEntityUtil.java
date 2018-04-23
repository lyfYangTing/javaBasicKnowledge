package util.poi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.poi.exception.PoiExtractException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 18435 on 2018/3/29.
 */
public class ExcelExtractForEntityUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExtractForEntityUtil.class);

    /**
     * 解析excel数据  以Object对象存储数据
     *
     * @param sheetNum
     * @param startRow
     * @return
     * @throws PoiExtractException
     */
    public static <T> List<T> getObjectData(File file, int sheetNum, int startRow, Class<T> tClass) throws PoiExtractException {
        Workbook wb = getWorkbook(file);
        List<T> data = new ArrayList<T>();
        Sheet sheet = wb.getSheetAt(sheetNum);
        if (sheet == null) {
            throw new PoiExtractException("未找到sheet at: " + sheetNum);
        }
        startRow = startRow > 0 ? startRow : 1;
        Row headRow = sheet.getRow(0);
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            try {
                T obj = tClass.newInstance();
                Map<String, String> cellData = new HashMap<String, String>();
                if (row == null) {//如果中间有空行则跳过本行
                    continue;
                }
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    String headValue = getCellValue(headRow.getCell(j));
                    String cellValue = getCellValue(row.getCell(j));
                    cellData.put(headValue, cellValue);
                    logger.info("headValue = {}",headValue);
                    obj = JSONObject.parseObject(JSON.toJSONString(cellData), tClass);
                }
                data.add(obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 获取单列数据
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula();
                    break;
            }
        }
        return value;
    }

    /**
     * 获取工作表
     *
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) {
        String fileName = file.getName();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        Workbook workbook = null;
        FileInputStream inputStream = null;
        try {
            inputStream = openInputStream(file);
            if ("xls".equals(fileType)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if ("xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                logger.error("文件格式错误");
                throw new PoiExtractException("文件格式错误");
            }
        } catch (IOException e) {
            e.getMessage();
        }

        return workbook;
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            } else if (!file.canRead()) {
                throw new IOException("File \'" + file + "\' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File \'" + file + "\' does not exist");
        }
    }

    public static void main(String[] args) {
        //F:\gitCloneProject\javaBasicKnowledge\resources\logExcel
        File file = new File("F://gitCloneProject/javaBasicKnowledge/resources/logExcel/2018-03-29数据.xls");
        Person person = new Person();
        List<Person> list = ExcelExtractForEntityUtil.getObjectData(file,0,1,Person.class);
        for(Person person1 : list){
            System.out.println(person1);
        }

    }
}
