package util.poi;


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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel数据提取
 *
 * @author luffy
 * @date 15/11/18
 */
public class ExcelExtractUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExtractUtil.class);

    /**
     * 解析excel数据
     * @param file
     * @return
     * @throws PoiExtractException
     */
    public static List<String[]> getData(File file) throws PoiExtractException {
        return getData(file, 0, 1);
    }

    /**
     * 解析excel数据
     * @param file
     * @param sheetNum
     * @param startRow
     * @return
     * @throws PoiExtractException
     */
    public static List<String[]> getData(File file, int sheetNum, int startRow) throws PoiExtractException {
        List<String[]> data = new ArrayList<String[]>();
        Workbook wb = null;
        try{
            InputStream is = new FileInputStream(file);
            String fileName = file.getName();
            if(fileName.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else if(fileName.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                throw new PoiExtractException("Excel文件格式不正确");
            }

            Sheet sheet = wb.getSheetAt(sheetNum);
            if(sheet == null) {
                throw new PoiExtractException("未找到sheet at: " + sheetNum);
            }

            for(int i = startRow; i<=sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String[] cellData = new String[row.getLastCellNum()+1];
                for(int j=0; j<=row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    cellData[j] = getCellValue(cell);
                }
                data.add(cellData);
            }
        } catch (PoiExtractException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new PoiExtractException(e);
        } finally {
            if(wb != null) try {
                wb.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new PoiExtractException(e);
            }
        }
        return data;
    }

    /**
     * 获取单列数据
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        String value = "";
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
        return value;
    }
}
