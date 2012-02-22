package org.inftel.ssa.services;

import java.sql.Connection;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.inftel.ssa.domain.*;

/**
 *
 * @author ibaca
 */
@Startup
@Singleton
@LocalBean
public class ConfigServiceImpl {

	private final static Logger logger = Logger.getLogger(ConfigServiceImpl.class.getName());
	@PersistenceContext(unitName = "ssa-persistence-unit")
	private EntityManager em;
	private String dataSet = "sample-dataset.xml";
	@EJB
	private SessionService sessionService;
	@EJB
	private ResourceService resourceService;

	@PostConstruct
	public void init() {
		try {
			secureInit();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Se han producido errores generando datos iniciales en bbdd. "
					+ " Normalmente esto se produce porque no se han reconstruido las tablas y por "
					+ "tanto estas contienen datos que hacen colisionar las inserciones. La "
					+ "aplicacion debe funcionar correctamente, pero es posible que el usuario "
					+ "demo@mail.com no este creado o se haya modificado.", e);
		}
	}

	private void secureInit() {
		logger.info("Creando datos iniciales programaticamente...");

		logger.info("Creando usuario demo (mail=demo@mail.com, pass=demo)");
		User demo = new User();
		demo.setCompany("Demo Company");
		demo.setEmail("demo@mail.com");
		demo.setFullName("Demo Full Namez");
		demo.setNickname("demo");
		demo.setPassword("demo");
		demo.setUserRole("team");
		sessionService.saveUser(demo);

		logger.info("Creando proyecto Manhattan");
		Project manhattan = new Project();
		manhattan.setCompany("Instituto Carnegie");
		manhattan.setDescription("<h3>Concepción del Proyecto</h3><p style=\"margin-top: 0.4em; margin-right: 0px; margin-bottom: 0.5em; margin-left: 0px; line-height: 19px; color: rgb(0, 0, 0); font-family: sans-serif; font-size: 13px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; orphans: 2; text-align: -webkit-auto; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-size-adjust: auto; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); \">Los científicos nucleares<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Le%C3%B3_Szil%C3%A1rd\" title=\"Leó Szilárd\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Leó Szilárd</a>,<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Edward_Teller\" title=\"Edward Teller\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Edward Teller</a><span class=\"Apple-converted-space\"> </span>y<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Eugene_Wigner\" title=\"Eugene Wigner\" class=\"mw-redirect\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Eugene Wigner</a>, refugiados<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Jud%C3%ADo\" title=\"Judío\" class=\"mw-redirect\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">judíos</a><span class=\"Apple-converted-space\"> </span>provenientes de<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Hungr%C3%ADa\" title=\"Hungría\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Hungría</a><span class=\"Apple-converted-space\"> </span>creían que la energía liberada por la<a href=\"http://es.wikipedia.org/wiki/Fisi%C3%B3n_nuclear\" title=\"Fisión nuclear\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">fisión nuclear</a><span class=\"Apple-converted-space\"> </span>podía ser utilizada para la producción de bombas por los alemanes, por lo que persuadieron a<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Albert_Einstein\" title=\"Albert Einstein\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Albert Einstein</a>, el físico más famoso en Estados Unidos, para que advirtiera al presidente<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/Franklin_D._Roosevelt\" title=\"Franklin D. Roosevelt\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">Franklin D. Roosevelt</a><span class=\"Apple-converted-space\"> </span>de este peligro por medio de una carta que Szilárd escribió y fue enviada el<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/2_de_agosto\" title=\"2 de agosto\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">2 de agosto</a><span class=\"Apple-converted-space\"> </span>de<span class=\"Apple-converted-space\"> </span><a href=\"http://es.wikipedia.org/wiki/1939\" title=\"1939\" style=\"text-decoration: none; color: rgb(11, 0, 128); background-image: none; background-attachment: initial; background-origin: initial; background-clip: initial; background-color: initial; background-position: initial initial; background-repeat: initial initial; \">1939</a>. En respuesta a la advertencia, Roosevelt incrementó las investigaciones acerca de las implicaciones en la seguridad nacional de la fisión nuclear. Después de la detonación sobre Hiroshima, Einstein comentaría:<span class=\"Apple-converted-space\"> </span><i>\"debería quemarme los dedos con los que escribí aquella primera carta a Roosevelt.\"</i></p>");
		manhattan.setLabels(new HashSet<String>(Arrays.asList(new String[]{"nuclear", "bomba", "guerra"})));
		manhattan.setLicense("Licencia para Matar");
		manhattan.getLinks().put("WikiWar", "http://es.wikipedia.org/wiki/Proyecto_Manhattan");
		manhattan.getLinks().put("Bomba Atómica", "http://es.wikipedia.org/wiki/Bomba_at%C3%B3mica");
		manhattan.setName("Proyecto Manhattan");
		manhattan.setSummary("Investigación de bombas atómicas");
		manhattan.getUsers().add(demo);
		demo.getProjects().add(manhattan);
		resourceService.saveProject(manhattan);

		logger.info("Creando tarea Tarea Uno en Manhattan");
		Task uno = new Task();
		uno.setDescription("Tarea uno de demostración.");
		uno.setPriority(Integer.SIZE);
		uno.setProject(manhattan);
		manhattan.getTasks().add(uno);
		uno.setStatus(TaskStatus.TODO);
		uno.setSummary("Fabricar uranio enriquecido");
		uno.setUser(demo);
		demo.getTasks().add(uno);
		resourceService.saveTask(uno);

		logger.info("Creando tarea Tarea Dos en Manhattan");
		Task dos = new Task();
		dos.setDescription("Tarea dos de demostración.");
		dos.setPriority(Integer.SIZE);
		dos.setProject(manhattan);
		manhattan.getTasks().add(dos);
		dos.setStatus(TaskStatus.TODO);
		dos.setSummary("Realizar prototipo de bomba N");
		dos.setUser(demo);
		demo.getTasks().add(dos);
		resourceService.saveTask(dos);

		logger.info("Creando sprint Sprint Uno en Manhattan");
		Sprint sUno = new Sprint();
		sUno.setDescription("Sprint uno de ejemplo");
		sUno.setEndDate(new Date(System.currentTimeMillis() + 3200 * 10));
		sUno.setProject(manhattan);
		manhattan.getSprints().add(sUno);
		sUno.setBeginDate(new Date());
		resourceService.saveSprint(sUno);

		logger.info("Datos iniciales programaticos creados con exito");

		em.flush(); // se sincroniza base de datos para que los
		populateData(); // XML puedan referenciar los id anteriores
	}

	public void populateData() {
		logger.info("Agregando datos iniciales en la base de datos desde XML...");
		try {
			Connection wrap = em.unwrap(Connection.class);
			// FIXME mala solucion, pero obtener la conexion difiere de test a desplegado
			if (wrap == null) {
				wrap = ((EntityManagerImpl) (em.getDelegate())).getServerSession().getAccessor().getConnection();
			}
			IDatabaseConnection connection = new DatabaseConnection(wrap);
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			IDataSet dataset = builder.build(Thread.currentThread().getContextClassLoader().getResourceAsStream(getDataSet()));
			DatabaseOperation.INSERT.execute(connection, dataset);
			logger.info("Datos iniciales XML creados con exito");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Fallo mientras se intentaba añadir datos iniciales al modelo", e);
		}

	}

	private String getDataSet() {
		return dataSet;
	}
}
