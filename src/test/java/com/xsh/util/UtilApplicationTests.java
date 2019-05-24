package com.xsh.util;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private FTPUtil ftpUtil;
    /**
     * 本地存储路径
     */
    @Value("${ftp.local.save.basepath}")
    private String localSavePath;

    @Test
    public void ftpDownloadTest() {
        Map<String, Object> result = ftpUtil.downLoadTableFile("dw_account_district", localSavePath);
        TestCase.assertEquals(true, result.get("result"));
    }
}
