
package org.inftel.ssa.web.messages;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;


@ManagedBean
@SessionScoped

public class LanguageBean {
    
    
    private String locale;
    
    public void LanguageBean(){
        locale="es";
    }

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



