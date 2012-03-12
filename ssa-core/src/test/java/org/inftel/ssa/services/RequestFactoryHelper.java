
package org.inftel.ssa.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.mockito.ArgumentCaptor;

import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.server.SimpleRequestProcessor;
import com.google.web.bindery.requestfactory.server.testing.InProcessRequestTransport;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestFactory;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;
import com.google.web.bindery.requestfactory.vm.RequestFactorySource;

@SuppressWarnings("unchecked")
public class RequestFactoryHelper {

    private static class MockServiceLocator implements ServiceLocator {
        private final Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

        @Override
        public Object getInstance(Class<?> clazz) {
            // Make sure to return always the same mocked instance for each
            // requested type
            Object result = services.get(clazz);
            if (result == null) {
                result = mock(clazz);
                services.put(clazz, result);
            }
            return result;
        }
    }

    private static class MockServiceDecorator extends ServiceLayerDecorator {
        @Override
        public <T extends ServiceLocator> T createServiceLocator(Class<T> clazz) {
            return (T) serviceLocator;
        }
    }

    private static MockServiceLocator serviceLocator = new MockServiceLocator();
    private static ServiceLayer serviceLayer = ServiceLayer.create(new MockServiceDecorator());

    /**
     * Crea un {@link RequestFactory} decorada con un simulador.
     */
    public static <T extends RequestFactory> T createMockedFactory(Class<T> requestFactoryClass) {
        SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
        addExceptionHandler(processor);
        T factory = RequestFactorySource.create(requestFactoryClass);
        factory.initialize(new SimpleEventBus(), new InProcessRequestTransport(processor));
        return factory;
    }

    /**
     * Crea un {@link RequestFactory} que se comunica de forma directa, por
     * tanto no usa red y los eventos fire son sincronos.
     */
    public static <T extends RequestFactory> T createSimpleFactory(Class<T> requestFactoryClass) {
        ServiceLayer serviceLayer = ServiceLayer.create();
        SimpleRequestProcessor processor = new SimpleRequestProcessor(serviceLayer);
        addExceptionHandler(processor);
        T factory = RequestFactorySource.create(requestFactoryClass);
        factory.initialize(new SimpleEventBus(), new InProcessRequestTransport(processor));
        return factory;
    }

    /**
     * @param requestProcessor
     */
    private static void addExceptionHandler(SimpleRequestProcessor requestProcessor) {
        ExceptionHandler eh = new SsaRequestFactoryServlet.SsaExceptionHandler();
        requestProcessor.setExceptionHandler(eh);
    }

    /**
     * Returns the same service instance as used by the RequestFactory
     * internals.
     */
    public static <T> T getService(Class<T> serviceClass) {
        T result = (T) serviceLocator.getInstance(serviceClass);
        reset(result); // reset mock to avoid side effects when used in multiple
                       // tests
        return result;
    }

    /**
     * Returns the value passed to {@link Receiver#onSuccess(Object)}
     */
    public static <T> T captureResult(Receiver<T> receiver) {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(receiver).onSuccess((T) captor.capture());
        return (T) captor.getValue();
    }
}
