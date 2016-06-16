/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.przemyslawzawadzki.listener;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 *
 * @author Przemek DELL
 */
public class AttributeListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {

        String attributeName = event.getName();
        Object attributeValue = event.getValue();
        System.out.println("Attribute added : " + attributeName + " : " + attributeValue);

        if (attributeName.equals("login")) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

            try {
                ec.redirect(ec.getRequestContextPath() + "/private/index.xhtml");
            } catch (IOException ex) {
                //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        String attributeName = event.getName();
        Object attributeValue = event.getValue();
        System.out.println("Attribute removed : " + attributeName + " : " + attributeValue);
        if ("login".equals(attributeName)) {

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            try {
                ec.redirect(ec.getRequestContextPath() + "/public/index.xhtml");
            } catch (IOException ex) {
                //Logger.getLogger(AttributeListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
}
