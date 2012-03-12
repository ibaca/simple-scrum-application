
package org.inftel.ssa.services;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface SsaRequestFactory extends RequestFactory {

    UserRequest userRequest();
}
