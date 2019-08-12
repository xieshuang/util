package com.xsh.util.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @description: mongo配置
 * @date: 2019/8/1
 **/
@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration{

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.one.database:aiwork}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(new MongoClientURI(uri));
    }

    @Bean("mongoTemplateAiWork")
    public MongoOperations mongoTemplateHw() throws Exception
    {
        MongoOperations mongoTemplate = new MongoTemplate(this.mongo(), database);
        MappingMongoConverter mmc = (MappingMongoConverter) mongoTemplate.getConverter();
        mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mongoTemplate;
    }
}
