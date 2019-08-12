package com.xsh.util.entity.mongo;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @description:
 * @date: 2019/8/1
 * @author: shuangxie@iflytek.com
 **/
public class MongoBook {

    @Field("bookName")
    private String bookName;

    @Field("pageNum")
    private int pageNum;

    @Field("title")
    private String title;

    public MongoBook() {
    }

    public MongoBook(String bookName, int pageNum, String title) {
        this.bookName = bookName;
        this.pageNum = pageNum;
        this.title = title;
    }

    /**
     * 获取 @Field("bookName")
     *
     * @return bookName @Field("bookName")
     */
    public String getBookName() {
        return this.bookName;
    }

    /**
     * 设置 @Field("bookName")
     *
     * @param bookName @Field("bookName")
     */
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * 获取 @Field("pageNum")
     *
     * @return pageNum @Field("pageNum")
     */
    public int getPageNum() {
        return this.pageNum;
    }

    /**
     * 设置 @Field("pageNum")
     *
     * @param pageNum @Field("pageNum")
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 获取 @Field("title")
     *
     * @return title @Field("title")
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 设置 @Field("title")
     *
     * @param title @Field("title")
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
