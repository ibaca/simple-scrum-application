package org.inftel.ssa.datamining;

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
import org.junit.*;

/**
 *
 */
public class DataminingEndOfDayTest {

  static EntityManagerFactory emf;
  static EntityManager em;
  static IDatabaseConnection connection;
  static IDataSet dataset;
  static EntityTransaction tx;

  public DataminingEndOfDayTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    // Inicializar EntityManager, obtener connection y cargar datos XML
    emf = Persistence.createEntityManagerFactory("ssa-datamining-test");
    em = emf.createEntityManager();
    connection = new DatabaseConnection(
            ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection());
    FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
    dataset = builder.build(
            Thread.currentThread().getContextClassLoader().getResourceAsStream("datamining-test-dataset.xml"));
    // Todo lo realizado hasta ahora se puede hacer una unica vez para todos los test
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    em.close();
    emf.close();
  }

  @Before
  public void setUp() throws Exception {
    //DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);
    DatabaseOperation.DELETE_ALL.execute(connection, connection.createDataSet());
    tx = em.getTransaction();
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of myTimer method, of class EndOfDayStatisticsTimer.
   */
  @Test
  public void testMyTimer() throws Exception {
 
    
  }
}
