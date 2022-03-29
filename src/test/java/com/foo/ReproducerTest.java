package com.foo;

import com.antwerkz.bottlerocket.BottleRocket;
import com.antwerkz.bottlerocket.BottleRocketTest;
import com.github.zafarkhaja.semver.Version;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class ReproducerTest extends BottleRocketTest {
    private Datastore datastore;

    public ReproducerTest() {
        MongoClient mongo = getMongoClient();
        MongoDatabase database = getDatabase();
        database.drop();
        datastore = Morphia.createDatastore(mongo, getDatabase().getName());
        
        //problem code
        InnerEntity  innerEntity = new InnerEntity();
        Query<InnerEntity> query = datastore.createQuery(InnerEntity.class)
            .field("_id").equal(new ObjectId("123123"));

        //when InnerEntity-->my_entity-->api_name  is [], 'api_name' will be filtered out in updateOperations 
    UpdateOperations<InnerEntity> updateOperations = mongoContext.createUpdateOperations(InnerEntity.class)
            .set("name","yxw" )
            .set("my_entity", innerEntity.getMyEntity);
    }

    @NotNull
    @Override
    public String databaseName() {
        return "morphia_repro";
    }

    @Nullable
    @Override
    public Version version() {
        return BottleRocket.DEFAULT_VERSION;
    }

    @Test
    public void reproduce() {
    }

}
