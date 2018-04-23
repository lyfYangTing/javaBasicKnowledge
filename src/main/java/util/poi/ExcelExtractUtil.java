package util.poi;


import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
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
import java.util.*;

/**
 * Excel数据提取
 *
 * @author luffy
 * @date 15/11/18
 */
public class ExcelExtractUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExtractUtil.class);

    /**
     * 读取excel中的所有sheet表中的数据  一行数据是一个String[]
     *
     * @param file excel文件
     * @return
     * @throws PoiExtractException
     */
    public static Map<String, List<String[]>> getListData(File file) throws PoiExtractException {
        Workbook workbook = null;
        Map<String, List<String[]>> sheetMap = new HashedMap();
        try {
            workbook = getWorkbook(file.getName(), file);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                List<String[]> data = getData(workbook, i, 1);
                sheetMap.put("sheet" + i, data);
            }
            return sheetMap;
        } catch (PoiExtractException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (workbook != null) try {
                workbook.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new PoiExtractException(e);
            }
        }
    }

    /**
     * 解析excel数据  以String[]数组存储数据
     *
     * @param sheetNum
     * @param startRow
     * @return
     * @throws PoiExtractException
     */
    public static List<String[]> getData(Workbook wb, int sheetNum, int startRow) throws PoiExtractException {
        List<String[]> data = new ArrayList<String[]>();
        Sheet sheet = wb.getSheetAt(sheetNum);
        if (sheet == null) {
            throw new PoiExtractException("未找到sheet at: " + sheetNum);
        }
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {//如果中间有空行则跳过本行
                continue;
            }
            String[] cellData = new String[row.getLastCellNum()];
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                cellData[j] = getCellValue(cell);
            }
            logger.info("sheetNumber :" + sheetNum + "第" + i + "行的数据 :" + Arrays.toString(cellData));
            data.add(cellData);
        }
        return data;
    }


    /**
     * 读取excel中的所有sheet表中的数据  一行数据是一个String[]
     *
     * @param file excel文件
     * @return
     * @throws PoiExtractException
     */
    public static Map<String, List<Map<String, String>>> getMapData(File file) throws PoiExtractException {
        Workbook workbook = null;
        Map<String, List<Map<String, String>>> sheetMap = new HashedMap();
        try {
            workbook = getWorkbook(file.getName(), file);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                List<Map<String, String>> data = getMapData(workbook, i, 1);
                sheetMap.put("sheet" + i, data);
            }
            return sheetMap;
        } catch (PoiExtractException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (workbook != null)
                try {
                    workbook.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    throw new PoiExtractException(e);
                }
        }
    }

    /**
     * 解析excel数据  以Map集合存储数据
     *
     * @param sheetNum
     * @param startRow
     * @return
     * @throws PoiExtractException
     */
    public static List<Map<String, String>> getMapData(Workbook wb, int sheetNum, int startRow) throws PoiExtractException {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        Sheet sheet = wb.getSheetAt(sheetNum);
        if (sheet == null) {
            throw new PoiExtractException("未找到sheet at: " + sheetNum);
        }
        Row headRow = sheet.getRow(0);
        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {//如果中间有空行则跳过本行
                continue;
            }
            Map<String, String> cellData = new HashMap<String, String>();
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                Cell headCell = headRow.getCell(j);
                cellData.put(getCellValue(headCell), getCellValue(cell));
            }
            logger.info("sheetNumber :" + sheetNum + "第" + i + "行的数据 :" + JSONArray.fromObject(cellData).toString());
            data.add(cellData);
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
    public static Workbook getWorkbook(String fileName, File file){
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        Workbook workbook = null;
        FileInputStream inputStream = null;
        try{
            inputStream = openInputStream(file);
            if ("xls".equals(fileType)) {
                workbook = new HSSFWorkbook(inputStream);
            } else if ("xlsx".equals(fileType)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                logger.error("文件格式错误");
                throw new PoiExtractException("文件格式错误");
            }
        }catch(IOException e){
            e.getMessage();
        }

        return workbook;
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if(file.exists()) {
            if(file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            } else if(!file.canRead()) {
                throw new IOException("File \'" + file + "\' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File \'" + file + "\' does not exist");
        }
    }

    public static void main(String[] args) {
        File file = new File("G://ceshi/POS机设备导入异常2018-03-20.xls");
        Person person = new Person();
//        Map<String,List<Map<String,String>>> data = ExcelExtractUtil.getMapData(file);
//        Set<String> set = data.keySet();
//        for(String key:set){
//            System.out.println(key+"的数据如下:");
//            List<Map<String,String>> list = data.get(key);
//            for(Map<String,String> rowData:list){
//                System.out.println(JSONArray.fromObject(rowData).toString());
//            }
//        }
//        List<Object> map = ExcelExtractUtil.getListData(file,person);
//        Set<String> set = map.keySet();
//        for (String key : set) {
//            System.out.println(key + "的数据如下:");
//            List<String[]> list = map.get(key);
//            for (String[] rowData : list) {
//                System.out.println(Arrays.toString(rowData));
//            }
//        }
    }
}
