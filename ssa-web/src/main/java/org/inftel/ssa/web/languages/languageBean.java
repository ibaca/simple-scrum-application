/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web.languages;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author JuaNaN
 */
@Stateless
@ManagedBean
@RequestScoped
public class languageBean {

  private String locale="es";

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getLocale() {
    return locale;
  }

  public String changeLanguage() {
    return null;
  }

}