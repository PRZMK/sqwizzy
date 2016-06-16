/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.beans;

import eu.przemyslawzawadzki.dao.PostDao;
import eu.przemyslawzawadzki.dao.UserDao;
import eu.przemyslawzawadzki.model.UserDTO;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Przemek DELL
 */
@ManagedBean
@SessionScoped
public class UserBean {

    public UserBean() {
    }

    UserDao userDao = new UserDao();

    private UserDTO user = new UserDTO();

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public void addInformation(String s) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, s, ""));
    }

    public void findUser() {
        user = userDao.findByLogin(user.getLogin());
        PostDao postDao = new PostDao();
        user.setPosts(postDao.getPosts(user.getLogin()));
    }

    public void userValidation() {

        UserDTO tempUser = userDao.findByLogin(user.getLogin());
        if (tempUser == null) {
            addInformation("wrong login!");
            return;
        } else if (!tempUser.getPassword().equals(user.getPassword())) {
            addInformation("wrong password!");
            return;
        }

        HttpSession session = SessionBean.getSession();

        session.setAttribute("name", tempUser.getName());
        session.setAttribute("lastname", tempUser.getLastName());
        session.setAttribute("email", tempUser.getEmail());
        session.setAttribute("login", tempUser.getLogin());
    }

    public void register() {
        userDao.addUser(user);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/public/index.xhtml");
        } catch (IOException ex) {
            //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void edit() {
        user = userDao.findByLogin((String) SessionBean.getSession().getAttribute("login"));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath() + "/private/setting.xhtml");
        } catch (IOException ex) {
            //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void update() {
        if(user.getPassword().isEmpty()){
            addInformation("insert password");
            user = userDao.findByLogin((String) SessionBean.getSession().getAttribute("login"));
            return;
        }
        userDao.update(user);
    }

    public void logout() {
        HttpSession session = SessionBean.getSession();
        session.invalidate();
    }
}
