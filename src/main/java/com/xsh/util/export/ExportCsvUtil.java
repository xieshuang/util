package com.xsh.util.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class ExportCsvUtil<T> {

    private static Logger log = LoggerFactory.getLogger(ExportCsvUtil.class);

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\r\n";

    /**
     * CSV文件列分隔符 避免出现科学计数法
     */
    private static final String CSV_NUMBER_NORMAL = "\t";

    public void export(String fileName, String[] headers, String[] columns,
                       List<T> lists, HttpServletResponse response) throws Exception {
        StringBuffer buf = new StringBuffer();
        // 输出列头
        for (int i = 0; i < headers.length; i++) {
            buf.append(headers[i]).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_RN);
        Iterator<T> it = lists.iterator();
        while (it.hasNext()) {
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
                        String textVal = "";
                        if (null != val) {
                            textVal = val.toString();
                        } else {
                            textVal = "";
                        }
                        buf.append(textVal);
                    }
                }
                buf.append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
        }
        exportCsv(fileName, buf.toString(), response);
    }

    public static void exportCsv(String fileName, String content,
                                 HttpServletResponse response) {
        OutputStream os = null;
        try {
            response.reset();
            // 设置文件后缀
            String fn = fileName + ".csv";

            // 设置响应
            response.setContentType("text/csv;");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "max-age=30");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fn.getBytes("gb2312"), "iso8859-1"));
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            // 写出响应
            os = response.getOutputStream();
            os.write(content.getBytes("GBK"));
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("财务流水导出CSV出错：", e);
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                log.error("财务流水导出CSV关闭输出流出错：", e);
            }
        }
    }

}
