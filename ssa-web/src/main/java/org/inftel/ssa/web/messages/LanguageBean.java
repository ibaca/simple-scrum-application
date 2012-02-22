
package org.inftel.ssa.web.messages;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@Stateless
@ManagedBean
@RequestScoped
public class LanguageBean {

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