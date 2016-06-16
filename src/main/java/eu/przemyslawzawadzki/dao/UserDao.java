/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.dao;

import com.mongodb.BasicDBObject;

import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import eu.przemyslawzawadzki.model.UserDTO;
import eu.przemyslawzawadzki.util.MongoConector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Przemek DELL
 */
public class UserDao {

    private MongoCollection userCollection = MongoConector.getCollection("user");

    public List<UserDTO> getUsers() {
        List<UserDTO> list = new ArrayList<>();
        UserDTO user = null;
        FindIterable<Document> iterable = userCollection.find();
        for (Document document : iterable) {
            user = new UserDTO();
            user.setLogin(document.getString("_id"));
            user.setPassword(document.getString("password"));
            user.setName(document.getString("name"));
            user.setLastName(document.getString("lastname"));
            user.setEmail(document.getString("email"));
            list.add(user);
        }
        return list;
    }

    public void saveUser(UserDTO user) {
        Document document;
        document = new Document("_id", user.getLogin())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("lastname", user.getLastName())
                .append("email", user.getEmail());
        userCollection.insertOne(document);
    }
    public UserDTO findByLogin(String id) {
        FindIterable<Document> iterable = userCollection.find(eq("_id", id));
        Document document = iterable.first();
        UserDTO user = new UserDTO();
        user.setLogin(document.getString("_id"));
        user.setPassword(document.getString("password"));
        user.setName(document.getString("name"));
        user.setLastName(document.getString("lastname"));
        user.setEmail(document.getString("email"));
       
        return user;
    }
    
    public void addUser(UserDTO user){
        Document document;
        document = new Document("_id", user.getLogin())
                .append("password", user.getPassword())
                .append("name", user.getName())
                .append("lastname", user.getLastName())
                .append("email", user.getEmail());
        userCollection.insertOne(document);
    }

    public void update(UserDTO user) {
        
        userCollection.updateOne(new Document("_id", user.getLogin()),
                new Document("$set", new Document("name", user.getName())
                        .append("password", user.getPassword())
                        .append("lastname", user.getLastName())
                        .append("email", user.getEmail())));
//        userCollection.updateOne(new Document("_id", user.getId()), new Document("$set", new Document("lastname", user.getLastName())));
//        userCollection.updateOne(new Document("_id", user.getId()), new Document("$set", new Document("email", user.getEmail())));
    }
    
 
    

}
