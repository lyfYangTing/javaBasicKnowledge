package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel实用类
 * Created by Yuan on 2015/7/8 0008.
 */
public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static <T> List<T> read(String excelName, File excelFile,  Class<T> entityClass, String[] propNames) {
        List<T> list = new ArrayList<>();

        try {
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = getWorkbook(excelName, inputStream);
            if (workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);//只有一个工作区
                // 计算共多少行
                Row firstRow = sheet.getRow(0);
                int cells = firstRow.getPhysicalNumberOfCells();

                //从第二行开始
                for (int rowNum = 1, j = sheet.getLastRowNum(); rowNum <= j; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }

                    T entity = entityClass.newInstance();
                    for (int cellNum = 0; cellNum < cells; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String propName = propNames[cellNum];

                        //PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(entityClass, propName);
                       //propertyDescriptor.getWriteMethod().invoke(entity, getValue(cell));
                    }

                    list.add(entity);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return list;
    }

    /**
     * 获取工作表
     *
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbook(String fileName, FileInputStream inputStream) throws IOException {
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        Workbook workbook = null;
        if ("xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if ("xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            logger.error("文件格式错误");
        }
        return workbook;
    }

    /**
     * 获取列值
     *
     * @param cell
     * @return
     */
    private static String getValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(cell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(cell.getStringCellValue());
        }
    }


    /**
     * 读取CSV文件
     *
     * @param csvFilePath
     *            文件路径
     * @param fileEncoder
     *            读取文件编码方式，主要是为了解决中文乱码问题。
     * @param separtor
     *            CSV的分隔符，默认是逗号
     */
    public static List<Map> readCsvFile(String csvFilePath, String fileEncoder, String separtor) {
        InputStreamReader fr = null;
        BufferedReader br = null;
        List<Map> list = new ArrayList<Map>();

        try {
            fr = new InputStreamReader(new FileInputStream(csvFilePath), fileEncoder);
            br = new BufferedReader(fr);
            String rec = null;
            String[] argsArr = null;
            int a = 0;
            while ((rec = br.readLine()) != null) {
                if(a>4){
                    Map<String,String > map = new HashMap<String, String>();
                    if (rec != null && rec.indexOf("#")==-1) {
                        argsArr = rec.split(separtor);
                        for (int i = 0; i < argsArr.length; i++) {
                            // System.out.print((i + 1) + ":" + argsArr[i] + "\t");
                            map.put(i+"",argsArr[i]);
                        }
                        //System.out.println();
                        list.add(map);
                    }
                }else {
                    a++;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (br != null)
                    br.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 读取cvs文件
     * @param csvFilePath
     * @param fileEncoder
     * @param separtor
     * @param check
     * @return
     */
    public static List<Map> readCsvFileCheck(String csvFilePath, String fileEncoder, String separtor,String check){
        InputStreamReader fr = null;
        BufferedReader br = null;
        List<Map> list = new ArrayList<Map>();
        try {
            fr = new InputStreamReader(new FileInputStream(csvFilePath), fileEncoder);
            br = new BufferedReader(fr);
            String rec = null;
            String[] argsArr = null;
            while ((rec = br.readLine()) != null) {
                    Map<String,String > map = new HashMap<String, String>();
                    if (rec != null && rec.indexOf(check)!=-1) {
                        argsArr = rec.split(separtor);
                        for (int i = 0; i < argsArr.length; i++) {
                            map.put(i+"",argsArr[i]);
                        }
                        list.add(map);
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (br != null)
                    br.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }
}
