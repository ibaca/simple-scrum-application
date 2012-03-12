
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
            return lookupBean(clazz, clazz.getSimpleName());
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T lookupBean(Class<T> clazz, String name) {
        try {
            // try {
            // return clazz.newInstance();
            // } catch (InstantiationException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (IllegalAccessException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            return (T) InitialContext.doLookup(portableName + name);
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
