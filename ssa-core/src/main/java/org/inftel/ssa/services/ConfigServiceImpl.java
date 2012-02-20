package org.inftel.ssa.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskStatus;
import org.inftel.ssa.domain.User;

/**
 *
 * @author ibaca
 */
@Startup
@Singleton
@LocalBean
public class ConfigServiceImpl {
	
	private final static Logger logger = Logger.getLogger(ConfigServiceImpl.class.getName());

	@EJB
	private SessionService sessionService;
	@EJB
	private ResourceService resourceService;
	
	@PostConstruct
	public void init() throws Exception {
		logger.info("Creando datos iniciales...");
		
		logger.info("Creando usuario demo (mail=demo@mail.com, pass=demo)");
		User demo = new User();
		demo.setCompany("Demo Company");
		demo.setEmail("demo@mail.com");
		demo.setFullName("Demo Full Namez");
		demo.setNickname("demo");
		demo.setPassword("demo");
		demo.setUserRole("team");
		demo.setTasks(new ArrayList<Task>());
		sessionService.saveUser(demo);
		
		logger.info("Creando proyecto Manhattan");
		Project manhattan = new Project();
		manhattan.setDescription("Proyecto cientifico durante la segurra Guerra Mundial.");
		manhattan.setLabels(new HashSet<String>(Arrays.asList(new String[]{"nuclear","bomba","guerra"})));
		manhattan.setLicense("Licencia para Matar");
		manhattan.setLinks(new HashMap<String, String>());
		manhattan.getLinks().put("WikiWar", "http://es.wikipedia.org/wiki/Proyecto_Manhattan");
		manhattan.getLinks().put("Bomba Atómica", "http://es.wikipedia.org/wiki/Bomba_at%C3%B3mica");
		manhattan.setName("Proyecto Manhattan");
		manhattan.setSummary("Proyecto para la investigación de bombas atómicas");
		manhattan.setUsers(new HashSet<User>());
		manhattan.getUsers().add(demo);
		manhattan.setTasks(new ArrayList<Task>());
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
		
		logger.info("Datos iniciales creados con exito!");
	}
}
