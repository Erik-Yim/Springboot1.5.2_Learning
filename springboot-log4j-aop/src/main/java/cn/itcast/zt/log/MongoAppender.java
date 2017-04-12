package cn.itcast.zt.log;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.stereotype.Component;

/**
 * 日志插入MongoDB
 * Created by zhangtian on 2017/4/12.
 */
@Component
public class MongoAppender extends AppenderSkeleton {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<BasicDBObject> logsCollection;

    private String connectionUrl;
    private String databaseName;
    private String collectionName;

    @Override
    protected void append(LoggingEvent loggingEvent) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Hello World");
        if(mongoDatabase == null) {
            MongoClientURI connectionString = new MongoClientURI(connectionUrl);
            mongoClient = new MongoClient(connectionString);
            mongoDatabase = mongoClient.getDatabase(databaseName);
            logsCollection = mongoDatabase.getCollection(collectionName, BasicDBObject.class);
        }
        logsCollection.insertOne((BasicDBObject) loggingEvent.getMessage());
    }

    @Override
    public void close() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Hello Close");
        if(mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
