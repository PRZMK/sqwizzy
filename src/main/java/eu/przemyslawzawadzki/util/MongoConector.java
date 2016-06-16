/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.util;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemek DELL
 */
public class MongoConector {

    private static MongoClient instance;
    
    private MongoConector(){
        
    }
    public synchronized static MongoClient getClient() {
        if (instance == null){
            instance = new MongoClient();
            instance.setWriteConcern(WriteConcern.JOURNALED);
        }
        return instance;
    }
    
    public static MongoDatabase getDB() {
        return getClient().getDatabase("sqwizzyDB");
    }
    
    public static MongoCollection getCollection(String name){
        return getDB().getCollection(name);
    }

}
