/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.IOException;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.FactoryFinder;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import org.inftel.ssa.services.SessionService;

/**
 *
 * @author ibaca
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

	@EJB
	private SessionService sessionService;
	private ServletContext servletContext;
	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.servletContext = filterConfig.getServletContext();
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FacesContext ctx = getFacesContext(request, response);
		if (ctx != null) {
			ELContext elContext = ctx.getELContext();
			sessionService = (SessionService) elContext.getELResolver().getValue(elContext, null, "sessionManager");
		} 
		if (sessionService == null || sessionService.currentUser() == null) {
			System.out.println("AuthFilter: Usuario desconocidos");
			//filterConfig.getServletContext().getRequestDispatcher("/Session/login.xhtml").forward(request, response);
		} else if (filterConfig != null) {
			System.out.println("AuthFilter: Usuario " + sessionService.currentUser().getEmail() + " logeado");
			//chain.doFilter(request, response);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	// You need an inner class to be able to call FacesContext.setCurrentInstance
	// since it's a protected method
	private abstract static class InnerFacesContext extends FacesContext {

		protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}

	private FacesContext getFacesContext(ServletRequest request, ServletResponse response) {
		// Try to get it first	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			return facesContext;
		}

		FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

		// Doesn't set this instance as the current instance of FacesContext.getCurrentInstance 
		facesContext = contextFactory.getFacesContext(servletContext, request, response, lifecycle);

		// Set using our inner class
		InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);

		// set a new viewRoot, otherwise context.getViewRoot returns null
		UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "yourOwnID");
		facesContext.setViewRoot(view);

		return facesContext;
	}
}
