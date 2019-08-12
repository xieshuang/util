package com.xsh.util;

import com.google.gson.Gson;
import com.xsh.util.dao.MongoDao;
import com.xsh.util.entity.mongo.BathUpdateOptions;
import com.xsh.util.entity.mongo.MongoBook;
import com.xsh.util.entity.mongo.MongoTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @date: 2019/8/1
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTestJunit {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testConnect() {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        collectionNames.forEach(e -> System.out.println(e));
    }

    @Test
    public void insertTest() {
        MongoBook one = new MongoBook("书本1", 100, "书本1title");
        MongoBook two = new MongoBook("书本2", 100, "书本1title");
        ArrayList<MongoBook> books = new ArrayList<>();
        books.add(one);
        books.add(two);
        MongoTest mongoTest = new MongoTest("测试1", "9", books);
        try {
            mongoTemplate.insert(mongoTest, mongoTemplate.getCollectionName(MongoTest.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private MongoDao mongoDao;

    @Test
    public void batchUpdateTest() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("5d429f1860d1521674a1e17f"));
        MongoTest one = mongoTemplate.findOne(query, MongoTest.class);
        one.getBooks().get(0).setBookName("测试更新书名");
        //需要更新的元素集合
        List<BathUpdateOptions> list = new ArrayList();
        Gson gson = new Gson();
        //FIXME 这里根据id没有生效
        list.add(new BathUpdateOptions(Query.query(Criteria.where("id").is("5d429f1860d1521674a1e17f")), Update.update("userName", "测试2"), false, true));
        //里面的局部json文档先转json再用mongojson格式化即可
        list.add(new BathUpdateOptions(Query.query(Criteria.where("id").is("5d429f1860d1521674a1e17f")), Update.update("books", com.mongodb.util.JSON.parse(gson.toJson(one.getBooks()))), false, true));

        //批量的更新 这里建议可以分批次提交
        int n = mongoDao.bathUpdate(mongoTemplate, MongoTest.class, list);
        System.out.println("一共更新了：" + n);

    }

}
