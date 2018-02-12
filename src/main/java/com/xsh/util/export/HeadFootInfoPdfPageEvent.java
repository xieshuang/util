package com.xsh.util.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.IOException;

public class HeadFootInfoPdfPageEvent extends PdfPageEventHelper {

    //自定义传参数
    public String pdfName;
    public PdfTemplate tpl;
    BaseFont bfChinese;

    //无参构造方法
    public HeadFootInfoPdfPageEvent() {
        super();
    }

    //有参构造方法
    public HeadFootInfoPdfPageEvent(String PdfName) {
        super();
        this.pdfName = PdfName;
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onOpenDocument(PdfWriter writer, Document document) {
        tpl = writer.getDirectContent().createTemplate(100, 20);
    }

    //实现页眉和页脚的方法
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            PdfContentByte headAndFootPdfContent = writer.getDirectContent();
            headAndFootPdfContent.saveState();
            headAndFootPdfContent.beginText();
            //设置中文
            headAndFootPdfContent.setFontAndSize(bfChinese, 12);
            //文档页头信息设置
            float x = document.top(-20);
            float x1 = document.top(-5);
            //页头信息左面
//            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "爱乐奇", document.left(), x, 0);
            //页头信息中间
            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, pdfName, (document.right() + document.left()) / 2, x, 0);
            //文档页脚信息设置
            float y = document.bottom(-20);
            float y1 = document.bottom(-35);
            //添加页码
            //页脚信息中间
            headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "--第" + document.getPageNumber(),
                    (document.right() + document.left()) / 2, y1, 0);
            //在每页结束的时候把“第x页”信息写道模版指定位置
            headAndFootPdfContent.addTemplate(tpl, (document.right() + document.left()) / 2 + 15, y1);//定位“y页” 在具体的页面调试时候需要更改这xy的坐标
            headAndFootPdfContent.endText();
            headAndFootPdfContent.restoreState();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        //关闭document的时候获取总页数，并把总页数按模版写道之前预留的位置
        tpl.beginText();
        tpl.setFontAndSize(bfChinese, 12);
        tpl.showText("页,共" + Integer.toString(writer.getPageNumber() - 1) + "页--");
        tpl.endText();
        tpl.closePath();//sanityCheck();
    }
}
