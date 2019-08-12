package com.xsh.util.entity.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @description:
 * @date: 2019/8/1
 **/
@Document(collection = "MongoTest")
public class MongoTest implements Serializable{

    private static final long serialVersionUID = 6585293886926880706L;

    @Id
    private String id;

    @Field("userName")
    private String userName;

    @Field("age")
    private String age;

    @Field("books")
    private ArrayList<MongoBook> books;

    public MongoTest() {
    }

    public MongoTest(String userName, String age, ArrayList<MongoBook> books) {
        this.userName = userName;
        this.age = age;
        this.books = books;
    }

    /**
     * 获取 @Id
     *
     * @return id @Id
     */
    public String getId() {
        return this.id;
    }

    /**
     * 设置 @Id
     *
     * @param id @Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取 @Field("userName")
     *
     * @return userName @Field("userName")
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置 @Field("userName")
     *
     * @param userName @Field("userName")
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取 @Field("age")
     *
     * @return age @Field("age")
     */
    public String getAge() {
        return this.age;
    }

    /**
     * 设置 @Field("age")
     *
     * @param age @Field("age")
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 获取 @Field("books")
     *
     * @return books @Field("books")
     */
    public ArrayList<MongoBook> getBooks() {
        return this.books;
    }

    /**
     * 设置 @Field("books")
     *
     * @param books @Field("books")
     */
    public void setBooks(ArrayList<MongoBook> books) {
        this.books = books;
    }
}
