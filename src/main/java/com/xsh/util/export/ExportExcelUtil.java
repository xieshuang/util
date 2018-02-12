package com.xsh.util.export;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExportExcelUtil<T> {
    private static Logger log = LoggerFactory.getLogger(ExportExcelUtil.class);

    public void export(String sheetName,String[] headers, String[] columns,
            List<T> lists, HttpServletResponse response) throws Exception {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(15);

        //表头格式 居中
        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        //数值格式 居右 两位小数
        DataFormat df = wb.createDataFormat();
        CellStyle numberStyle = wb.createCellStyle();
        numberStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        numberStyle.setDataFormat(df.getFormat("#,##0.00"));

        //日期格式
        CellStyle cellStyle = wb.createCellStyle();
        DataFormat format= wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));

        //字符类型 居中
        CellStyle StringStyle = wb.createCellStyle();

        Row row = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = row.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }

        Iterator<T> it = lists.iterator();
        int rowIndex = 0;
        while (it.hasNext()) {
            rowIndex++;
            row = sheet.createRow(rowIndex);
            Cell  cell;
            T t = it.next();
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String fieldName = field.getName();
                for (int j = 0; j < columns.length; j++) {
                    if (fieldName.equals(columns[j])) {
                        String getMethodName = "get" + fieldName.substring(0, 1)
                                .toUpperCase() + fieldName.substring(1);
                        Class cls = t.getClass();
                        Method getMethod = cls.getMethod(getMethodName, new Class[]{});
                        Object val = getMethod.invoke(t, new Object[]{});
                        cell = row.createCell(j);
                        if (null != val) {
                            if(val instanceof java.math.BigDecimal){
                                cell.setCellStyle(numberStyle);
                                cell.setCellValue(Double.valueOf(val.toString()));
                            } else if(val instanceof java.sql.Timestamp){
                                cell.setCellStyle(cellStyle);
                                cell.setCellValue(getDateTimeOfStr(val.toString()));
                            } else {
                                cell.setCellStyle(StringStyle);
                                cell.setCellValue(val.toString());
                            }
                        }
                    }
                }
            }
        }

        String filename = sheetName + ".xls";
        downloadExcel(filename, response, wb);
    }


    /**
     * 下载。
     */
    public static void downloadExcel(String filename, HttpServletResponse response,
                                     SXSSFWorkbook wb) {
        ByteArrayOutputStream out = null;
        OutputStream outStream = null;
        try {
            response.reset();
            out = new ByteArrayOutputStream();
            wb.write(out);
            byte[] outArray;
            outArray = out.toByteArray();

            // 设定输出文件头
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes("gb2312"), "iso8859-1"));
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            response.setContentLength(outArray.length);
            outStream = response.getOutputStream();
            outStream.write(outArray);
            outStream.flush();
            outStream.close();
        } catch (Throwable e) {
            log.error("导出excel出错：", e);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != outStream) {
                    outStream.close();
                }
            } catch (IOException e) {
                log.error("导出excel关闭输出错：", e);
            }

        }

    }

    public static Date getDateTimeOfStr(String date){
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date strDate = null;
        try {
            strDate = timeformat.parse(date);
        } catch (ParseException e) {
            log.error("格式化日期出错：", e);
        }
        return strDate;
    }
}
