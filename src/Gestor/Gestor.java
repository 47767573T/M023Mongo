package Gestor;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by Moises on 22/04/2016.
 */
public class Gestor {

    public static void main( String args[] ) {

        MongoClient mongoClient;

        if (args.length == 0) {
            // connect to the local database server
            mongoClient = new MongoClient();
        } else {
            mongoClient = new MongoClient(new MongoClientURI(args[0]));
        }

        // get handle to "mydb" database
        MongoDatabase database = mongoClient.getDatabase("mydb");


        // get a handle to the "test" collection
        MongoCollection<Document> collection = database.getCollection("test");

        System.out.println("Collection has " + collection.count() + " documents");

        // close resources
        mongoClient.close();
        System.out.println("======= Finish =======");

        }

}
