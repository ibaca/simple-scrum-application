package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.services.SessionService;

/**
 * Encargado de la gestion de usuarios. Sus principales funciones son
 *
 * <ul>
 *
 * <li>Obtener los datos del usuario actual autenticado</li>
 *
 * <li>Autenticar (login) o cerrar sesion (logout)</li>
 *
 * <li>Crear un nuevo usuario (signin)</li>
 *
 * </ul>
 *
 * @author ibaca
 */
@ManagedBean
@SessionScoped
public class UserManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private final static Logger logger = Logger.getLogger(UserManager.class.getName());
	@EJB
	private SessionService sessionService;
	// email y password usados en login
	private String email;
	private String password;
	// usuario usado para dar de alta
	private User signinUser;

	public UserManager() {
		logger.info("new user session created");
		signinUser = new User();
	}

	/**
	 * Devolvera cierto si actualmente hay un usuario autenticado.
	 */
	public boolean isAuthenticated() {
		return getCurrentUser() != null;
	}

	// No es buena idea mantener currentUser, pero si se hace se debe ir actualizando refrescando
	public User getCurrentUser() {
		Subject subject;
		if ((subject = SecurityUtils.getSubject()).isAuthenticated()) {
			return sessionService.findUser(subject.getPrincipal());
		} else {
			return null;
		}
	}

	public String login() {
		Subject current = SecurityUtils.getSubject();
		FacesContext context = FacesContext.getCurrentInstance();
		Set<Project> tmpProjects = null;

		logger.info("usuario actual authenticated? " + current.isAuthenticated());
		if (!current.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(email, password);
			//token.setRememberMe(true);
			try {
				current.login(token);
				//if no exception, that's it, we're done!
				logger.info("user " + email + " autenticated");
				this.email = "";
				this.password = "";
			} catch (UnknownAccountException uae) {
				//username wasn't in the system, show them an error message?
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autenticación incorrecta", "Usuario desconocdio"));
				logger.info("Usuario desconocdio");
			} catch (IncorrectCredentialsException ice) {
				//password didn't match, try again?
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autenticación incorrecta", "Clave incorrecta"));
				logger.info("Clave incorrecta");
			} catch (LockedAccountException lae) {
				//account for that username is locked - can't login.  Show them a message?
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autenticación incorrecta", "Cuenta de usuario bloqueada"));
				logger.info("Cuenta de usuario bloqueada");
			} catch (AuthenticationException ae) {
				//unexpected condition - error?
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fallo de autenticación", ae.getLocalizedMessage()));
				logger.info("Fallo de autenticación");
			}
		}
		if (!current.isAuthenticated()) {
			return null; // no se ha conseguido autenticar
		} else if ((tmpProjects = getCurrentUser().getProjects()).isEmpty()) {
			return "project/create?faces-redirect=true";
		} else {
			// TODO mostrar el ultimo proyecto visitado
			ProjectManager projectManager = (ProjectManager) context.getELContext().getELResolver().getValue(context.getELContext(), null, "projectManager");
			projectManager.setCurrentProject(tmpProjects.iterator().next());
			return "project/home?faces-redirect=true";
		}
	}

	public String goSignin() {
		signinUser = new User();
		signinUser.setEmail(email);
		signinUser.setPassword(password);
		return "signin?faces-redirect=true";
	}

	//TODO deberia ser transaccional, porq si falla el login no deberia dejar el usuario en bbdd
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String signin() {
		try {
			sessionService.saveUser(signinUser);
			logger.info("usuario " + signinUser + " registrdo");
			// Se hace login del usuario recien creado
			this.email = signinUser.getEmail();
			this.password = signinUser.getPassword();
			return login();
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Problema creando cuenta", e.getCause().getLocalizedMessage()));
			logger.log(Level.WARNING, "Problema creando usuario: " + signinUser, e);
			// Al hacer rollback deja estos valores configurados y no permite guardarlo de nuevo
			signinUser.setVersion(null);
			signinUser.setId(null);
		}
		return null;
	}

	public String logout() {
		logger.info("desconectando usuario " + getCurrentUser());
		SecurityUtils.getSubject().logout();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login.jsp?faces-redirect=true";
	}

	public String loginReset() {
		this.email = null;
		this.password = null;
		return "login";
	}

	public String signinReset() {
		this.signinUser = new User();
		return "signin";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getSigninUser() {
		return signinUser;
	}

	public void setSigninUser(User signinUser) {
		this.signinUser = signinUser;
	}
}
