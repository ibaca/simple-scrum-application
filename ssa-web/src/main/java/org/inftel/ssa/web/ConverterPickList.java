/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskFacade;
import org.inftel.ssa.services.ResourceService;

/**
 *
 * @author Jesus Ruiz Oliva
 */
public class ConverterPickList implements Converter {
    @EJB
    private ResourceService resources;
    

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("")) {
            return null;
        } else {
            return resources.findTask(new Long(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Task) value).getId());
        }
    }


   
}

