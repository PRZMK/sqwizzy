package eu.przemyslawzawadzki.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Przemek DELL
 */
@ManagedBean
@ViewScoped
public class SessionBean {

    public static HttpSession getSession() {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    public String getUserName() {
        HttpSession session = getSession();
        if (session != null) {
            return (String) session.getAttribute("login");
        } else {
            return "";
        }
    }

    public String getUserId() {
        HttpSession session = getSession();
        if (session != null && session.getAttribute("userId")!= null) {
            
            return session.getAttribute("userId").toString();
        } else {
            return "false";
        }
    }
    
    public String logout(){
        HttpSession session = getSession();
        session.removeAttribute("login");
        session.invalidate();
        
        return null;
    }
}
