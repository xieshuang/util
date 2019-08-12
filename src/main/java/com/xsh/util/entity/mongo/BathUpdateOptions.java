package com.xsh.util.entity.mongo;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @description: mongodb批量更新选项类
 **/
public class BathUpdateOptions {
    private Query query;
    private Update update;
    private boolean upsert = false;
    private boolean multi = false;

    public BathUpdateOptions(){

    }

    public BathUpdateOptions(Query query, Update update, boolean upsert, boolean multi){
        this.query = query;
        this.update = update;
        this.upsert = upsert;
        this.multi = multi;
    }
    /**
     * 获取
     *
     * @return query
     */
    public Query getQuery() {
        return this.query;
    }

    /**
     * 设置
     *
     * @param query
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * 获取
     *
     * @return update
     */
    public Update getUpdate() {
        return this.update;
    }

    /**
     * 设置
     *
     * @param update
     */
    public void setUpdate(Update update) {
        this.update = update;
    }

    /**
     * 获取
     *
     * @return upsert
     */
    public boolean isUpsert() {
        return this.upsert;
    }

    /**
     * 设置
     *
     * @param upsert
     */
    public void setUpsert(boolean upsert) {
        this.upsert = upsert;
    }

    /**
     * 获取
     *
     * @return multi
     */
    public boolean isMulti() {
        return this.multi;
    }

    /**
     * 设置
     *
     * @param multi
     */
    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
