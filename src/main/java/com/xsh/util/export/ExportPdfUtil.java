package com.xsh.util.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ExportPdfUtil<T> {

    private static Logger log = LoggerFactory.getLogger(ExportPdfUtil.class);

    public void export(String sheetName, String[] headers, String[] columns,
                       List<T> lists, HttpServletResponse response) throws Exception {
        OutputStream os = null;
        Document document = new Document();
        String fileName = sheetName.concat(".pdf");

        response.reset(); //清空输出流
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(fileName.getBytes("gb2312"), "iso8859-1")); // 设定输出文件头
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setContentType("application/octet-stream"); // 定义输出类型
        os = response.getOutputStream();

        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font keyfont = new Font(bfChinese, 11, Font.BOLD);// 设置字体大小
        Font textfont = new Font(bfChinese, 9, Font.NORMAL);// 设置字体大小
        document.setPageSize(PageSize.A4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //添加页码
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setPageEvent(new HeadFootInfoPdfPageEvent(sheetName));
        document.open();
        PdfPTable table = this.createTable(new float[]{2f, 2.5f, 2.5f, 2f, 1.5f});
        //设置表头
        for (int i = 0; i < headers.length; i++) {
            table.addCell(this.createCell(headers[i], keyfont, Element.ALIGN_CENTER));
        }

        if (null != lists && lists.size() > 0) {
            Class classType = lists.get(0).getClass();
            int size = lists.size();
            for (int i = 0; i < size; i++) {
                T t = lists.get(i);
                for (int j = 0; j < columns.length; j++) {
                    //获得首字母
                    String firstLetter = columns[j].substring(0, 1).toUpperCase();
                    //获得get方法,getName,getAge等
                    String getMethodName = "get" + firstLetter + columns[j].substring(1);
                    Method method;
                    try {
                        //通过反射获得相应的get方法，用于获得相应的属性值
                        method = classType.getMethod(getMethodName, new Class[]{});
                        try {
                            //添加数据
                            Object val = method.invoke(t, new Object[]{});
                            String textVal = "";
                            if(null != val){
                                textVal = val.toString();
                            } else {
                                textVal = "";
                            }
                            table.addCell(createCell(textVal, textfont));
                        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
                            log.error("财务流水导出pdf出错：", e);
                            e.printStackTrace();
                        }
                    } catch (SecurityException | NoSuchMethodException e) {
                        log.error("财务流水导出pdf出错：", e);
                        e.printStackTrace();
                    }
                }
            }
        }
        //关闭流
        try {
            document.add(table);
            document.close();
            response.setContentLength(outputStream.size());
            outputStream.writeTo(os);
            os.flush();
        } catch (IOException e) {
            log.error("财务流水导出pdf出错：", e);
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("财务流水导出pdf出错：", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建表格
     *
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(520);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建一个表格对象
     *
     * @param colNumber 表格的列数
     * @return 生成的表格对象
     */
    public PdfPTable createTable(int colNumber) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(520);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 为表格添加一个内容
     *
     * @param value 值
     * @param font  字体
     * @param align 对齐方式
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    /**
     * 为表格添加一个内容
     *
     * @param value 值
     * @param font  字体
     * @return 添加的文本框
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }


}
