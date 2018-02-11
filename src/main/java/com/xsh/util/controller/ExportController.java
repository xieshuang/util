package com.xsh.util.controller;

import com.xsh.util.entity.FinanceExportBean;
import com.xsh.util.export.ExportCsvUtil;
import javassist.expr.NewArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping(path = "export")
public class ExportController {

    private static Logger log = LoggerFactory.getLogger(ExportController.class);

    @RequestMapping("/index")
    public String index(Model model) {
        return "export/export";
    }


    @RequestMapping("/exportData")
    public void exportData(HttpServletResponse response, @RequestParam("type") String type) {
        String sheetName = "财务流水";
        String[] headers = {"订单号", "流水号", "支付时间", "支付类别", "订单金额"};
        String[] columns = {"orderNo", "tradeId", "payTime", "tradeType", "tradeAmount"};

        List<FinanceExportBean> list = new ArrayList<>();

        //测试大数据量导出
        FinanceExportBean testBean = new FinanceExportBean();
        testBean.setOrderNo("测试编号");
        testBean.setTradeId("cash+test+001");
        testBean.setPayTime(new Timestamp(new Date().getTime()));
        testBean.setTradeAmount(new BigDecimal(10000));
        for(int i = 0; i < 100; i++){
            testBean.setOrderNo("testNo"+i);
            list.add(testBean);
        }
        try {
            if(type.equals("Excel")){
//                ExportExcelUtil<FinanceExportBean> util = new ExportExcelUtil<FinanceExportBean>();
//                util.export(sheetName, headers, columns, list, response);
            } else if(type.equals("Csv")){
                ExportCsvUtil<FinanceExportBean> util = new ExportCsvUtil<FinanceExportBean>();
                util.export(sheetName, headers, columns, list, response);
            } else {
                //PDF
//                ExportPdfUtil<FinanceExportBean> util = new ExportPdfUtil<FinanceExportBean>();
//                util.export(sheetName, headers, columns, list, response);
            }
        } catch (Exception e) {
            log.error("财务流水导出出错：", e);
            e.printStackTrace();
        }
    }
}
