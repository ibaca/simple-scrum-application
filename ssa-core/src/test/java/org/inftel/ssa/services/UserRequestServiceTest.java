
package org.inftel.ssa.services;

import static org.inftel.ssa.services.RequestFactoryHelper.createSimpleFactory;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class UserRequestServiceTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testCountSurfers() {
        SsaRequestFactory rf = createSimpleFactory(SsaRequestFactory.class);

        UserRequest request = rf.userRequest();
        request.countUsers().fire(new Receiver<Long>() {
            @Override
            public void onSuccess(Long response) {
                // success
            }

            @Override
            public void onFailure(ServerFailure error) {
                fail("fallo obteniendo contador: " + error.getMessage());
            }
        });
    }

}
