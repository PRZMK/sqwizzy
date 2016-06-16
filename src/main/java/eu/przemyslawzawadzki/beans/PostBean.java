/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.beans;

import eu.przemyslawzawadzki.dao.PostDao;
import eu.przemyslawzawadzki.model.CommentDTO;
import eu.przemyslawzawadzki.model.PostDTO;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.bson.types.ObjectId;

/**
 *
 * @author Przemek DELL
 */
@ManagedBean
@SessionScoped
public class PostBean {

    /**
     * Creates a new instance of PostBean
     */
    public PostBean() {
    }

    private PostDao postDao = new PostDao();

    private PostDTO post = new PostDTO();

    private CommentDTO commentDTO = new CommentDTO();

    public CommentDTO getCommentDTO() {
        return commentDTO;
    }

    public void setCommentDTO(CommentDTO commentDTO) {
        this.commentDTO = commentDTO;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }

    public void savePost() {
        Date date = new Date();
        date.setTime(new Date().getTime());
        post.setDate(date);
        post.setUserId((String) SessionBean.getSession().getAttribute("login"));
        postDao.save(post);
    }

    public List<PostDTO> getAllPosts() {
        addInformation("wywołano");
        return postDao.getAllPosts();
    }

    ;
    public List<PostDTO> getPosts() {
        String userId = (String) SessionBean.getSession().getAttribute("login");
        return postDao.getPosts(userId);
    }

    ;
    
     public List<PostDTO> getUserPosts(String userId) {
        return postDao.getPosts(userId);
    }

    ;
     public void addComment() {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "witajw add", ""));
         System.out.println("hej add comment");
         commentDTO.setUserId((String) SessionBean.getSession().getAttribute("login"));
         postDao.addCommentToPost(commentDTO, post);
         commentDTO.setComment(null);
    }

    public void findPost() {

        post = postDao.getCurrentPost(post.getId());
    }

    public void deletePost() {
        postDao.delete(post);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/private/timeline.xhtml");
        } catch (IOException ex) {
            //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editPost(){
        postDao.edit(post);
    }
            
    public void addInformation(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s, ""));
    }
}
