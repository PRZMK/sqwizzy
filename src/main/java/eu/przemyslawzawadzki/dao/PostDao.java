/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.dao;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import eu.przemyslawzawadzki.model.CommentDTO;
import eu.przemyslawzawadzki.model.PostDTO;
import eu.przemyslawzawadzki.util.MongoConector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Przemek DELL
 */
public class PostDao {

    private MongoCollection postCollection = MongoConector.getCollection("post");

    public List<PostDTO> getAllPosts() {
        List<PostDTO> list = new ArrayList<>();
        PostDTO post;
        FindIterable<Document> iterable = postCollection.find();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "getall", ""));
        for (Document document : iterable) {
            post = new PostDTO();

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, document.getString("post"), ""));
            post.setId(document.get("_id", ObjectId.class).toString());
            post.setPost(document.getString("post"));
            post.setDate(document.get("date", Date.class));
            post.setLikes(document.get("likes", Integer.class));
            post.setUserId(document.getString("userid"));
            list.add(0, post);
        }

        return list;
    }

    public List<PostDTO> getPosts(String userId) {
        List<PostDTO> list = new ArrayList<>();
        PostDTO post;
        FindIterable<Document> iterable;
        iterable = postCollection.find((eq("userid", userId)));
        CommentDTO commentDTO = null;
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "witaj", ""));
        for (Document document : iterable) {
            post = new PostDTO();

            post.setId(document.get("_id", ObjectId.class).toString());
            post.setPost(document.getString("post"));
            post.setDate(document.get("date", Date.class));
            post.setLikes(document.get("likes", Integer.class));
            post.setUserId(document.getString("userid"));
            try {

                ArrayList<Document> comments = document.get("comment", ArrayList.class);
                for (Document comment : comments) {
                    commentDTO = new CommentDTO();
                    commentDTO.setComment(document.getString("post"));
                    commentDTO.setUserId(comment.getString("autor"));
                    commentDTO.setDate(comment.get("date", Date.class));
                    post.getComments().add(0, commentDTO);
                }
            } catch (NullPointerException e) {

            }

            list.add(0, post);
        }

        return list;
    }

    public PostDTO getCurrentPost(String postId) {
        List<PostDTO> list = new ArrayList<>();
        PostDTO post;
        FindIterable<Document> iterable;
        iterable = postCollection.find((eq("_id", new ObjectId(postId))));
        CommentDTO commentDTO = null;
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "witaj", ""));
        Document document = iterable.first();
        post = new PostDTO();

        post.setId(document.get("_id", ObjectId.class).toString());
        post.setPost(document.getString("post"));
        post.setDate(document.get("date", Date.class));
        post.setLikes(document.get("likes", Integer.class));
        post.setUserId(document.getString("userid"));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "witajbefor", ""));
        try {

            ArrayList<Document> comments = document.get("comment", ArrayList.class);

            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "try", ""));
            List<CommentDTO> commentList = new ArrayList<>();
            for (Document comment : comments) {
                commentDTO = new CommentDTO();
                commentDTO.setComment(comment.getString("post"));
                commentDTO.setUserId(comment.getString("autor"));
                commentDTO.setDate(comment.get("date", Date.class));
                commentList.add(0, commentDTO);
            }
            post.setComments(commentList);
        } catch (NullPointerException e) {

        }

        return post;
    }

    public void addCommentToPost(CommentDTO comment, PostDTO post) {

        Date date = new Date();
        date.setTime(new Date().getTime());
        postCollection.updateOne(new Document("_id", new ObjectId(post.getId())),
                new Document("$push", new Document("comment",
                        new Document("date",date)
                .append("autor", comment.getUserId())
                .append("post", comment.getComment()))));
        comment = null;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/private/post.xhtml?post="+post.getId());
        } catch (IOException ex) {
            //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void edit(PostDTO post){
        postCollection.updateOne(new Document("_id", new ObjectId(post.getId())),
        new Document("$set", new Document("post", post.getPost())));
    }

    public void save(PostDTO post) {
        Document document;
        document = new Document("post", post.getPost())
                .append("date", post.getDate())
                .append("userid", post.getUserId())
                .append("likes", 0);
        postCollection.insertOne(document);
    }

    public void delete(PostDTO post) {
        postCollection.deleteOne(eq("_id", new ObjectId(post.getId())));
    }
}
