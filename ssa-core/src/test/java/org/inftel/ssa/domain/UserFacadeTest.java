
package org.inftel.ssa.domain;

import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import static java.util.Locale.US;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ibaca
 */
public class UserFacadeTest {

    static EntityManagerFactory emf;
    static EntityManager em;
    static IDatabaseConnection connection;
    static IDataSet dataset;
    static EntityTransaction tx;

    public UserFacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Inicializar EntityManager, obtener connection y cargar datos XML
        emf = Persistence.createEntityManagerFactory("ssa-persistence-unit-test");
        em = emf.createEntityManager();
        connection = new DatabaseConnection(
                ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor()
                        .getConnection());
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        dataset = builder.build(
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("user-test-dataset.xml"));
        // Todo lo realizado hasta ahora se puede hacer una unica vez para todos
        // los test
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        em.close();
        emf.close();
    }

    @Before
    public void setUp() throws Exception {
        // La siguiente linea borra la tabla y escribe los datos del fichero XML
        // previamente configurado
        DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
        tx = em.getTransaction();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFind() throws Exception {
        UserFacade users = new UserFacade(em);
        assertEquals("Richard", users.find(1000l).getNickname());
    }

    @Test
    public void testFindAll() throws Exception {
        UserFacade users = new UserFacade(em);
        assertEquals(connection.getRowCount("users"), users.findAll().size());
    }

    @Test
    public void testAdvancedFind() {
        UserFacade users = new UserFacade(em);
        TaskFacade tasks = new TaskFacade(em);

        // Find all
        assertEquals(5, users.find(null, null, null, null, null).size());

        // Range
        assertEquals(3, users.find(null, 3, null, null, null).size());
        assertEquals(3, users.find(2, 3, null, null, null).size());
        assertEquals("Nick", users.find(2, 1, null, null, null).get(0).getNickname());

        // Order
        assertEquals("Syd", users.find(null, null, "email", null, null).get(0).getNickname());
        assertEquals("Roger", users.find(null, null, "email", false, null).get(0).getNickname());

        // Filter
        Map<String, String> filter = new IdentityHashMap<String, String>(1);
        filter.put("email", "gilmour%");
        assertEquals("David", users.find(null, null, null, null, filter).get(0).getNickname());
        assertEquals(0000001, users.find(0, 15, "nickname", true, filter).size());

        // Filter by relation
        Map<String, String> filter1 = new IdentityHashMap<String, String>(1);
        filter1.put("user.id", "1000");
        assertEquals("Tarea Ejemplo", tasks.find(null, null, null, null, filter1).get(0)
                .getSummary());
    }

    @Test
    public void testFindSice() throws ParseException {
        UserFacade users = new UserFacade(em);
        // Date 0 implica que debe devolver todos los valores
        assertEquals(users.findAll().size(), users.findSince(new Date(0)).size());

        // Date now no deberia devolver nada
        assertEquals(0, users.findSince(new Date()).size());

        // Filtra correctamente¿?
        assertEquals(2, users.findSince(getDateInstance(SHORT, US).parse("1/3/12")).size());
    }

}
