
package org.inftel.ssa.services;

import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Wrapper de {@link RequestFactoryServlet} para realizar log de los errores
 * producidos en los servicios solicitados por los clientes a traves de
 * <code>RequestFactory</code>.
 * 
 * @author ibaca
 */
public class SsaRequestFactoryServlet extends RequestFactoryServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(SsaRequestFactoryServlet.class.getName());

    /**
     * {@link ExceptionHandler} que registra los errores en el log.
     * 
     * @author ibaca
     */
    public static final class SsaExceptionHandler extends DefaultExceptionHandler {
        @Override
        public ServerFailure createServerFailure(Throwable throwable) {
            log.log(WARNING, "Service error: " + throwable.getMessage(), throwable);
            return super.createServerFailure(throwable);
        }
    }

    public SsaRequestFactoryServlet() {
        super(new SsaExceptionHandler());
        log.info("request factory servlet inicializado: " + this);
    }

}
