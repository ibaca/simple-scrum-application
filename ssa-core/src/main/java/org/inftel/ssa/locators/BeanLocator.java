
package org.inftel.ssa.locators;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class BeanLocator implements ServiceLocator {

    private static Package ssa = Package.getPackage("org.inftel.ssa.services");
    private static String portableName = "java:global/ssa-bundle/ssa-core-1.0-SNAPSHOT/";

    @Override
    public Object getInstance(Class<?> clazz) {
        System.out.println(clazz);
        if (clazz.getPackage().equals(ssa)) {
            return lookupBean(clazz);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookupBean(Class<T> clazz) {
        try {
            return (T) InitialContext.doLookup(portableName + clazz.getSimpleName());
        } catch (NamingException e) {
            throw new IllegalArgumentException("No existe el servicio " + clazz, e);
        }
    }

}
