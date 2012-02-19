package org.inftel.ssa.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import static junit.framework.Assert.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ibaca
 */
public class UserTest {

	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static EntityTransaction tx;

	@BeforeClass
	public static void initEntityManager() throws Exception {
		emf = Persistence.createEntityManagerFactory("ssa-persistence-unit-test");
		em = emf.createEntityManager();
	}

	@AfterClass
	public static void closeEntityManager() {
		em.close();
		emf.close();
	}

	@Before
	public void initTransaction() {
		tx = em.getTransaction();
	}

	@Test
	public void createUser() throws Exception {
		User user = new User();
		user.setFullName("ambrosio");
		user.setPassword("secreta");
		user.setEmail("create-user@mail.com");

		tx.begin();
		em.persist(user);
		tx.commit();

		assertNotNull("ID should not be null", user.getId());
	}

	@Test
	public void updateUser() throws Exception {
		User user = new User();
		user.setFullName("update-user");
		user.setPassword("update-user-clave");
		user.setEmail("update-user@mail.com");

		tx.begin();
		em.persist(user);
		tx.commit();

		String expectedResult;
		expectedResult = "update-user-modified";
		tx.begin();
		user.setFullName(expectedResult);
		tx.commit();

		assertEquals(expectedResult, em.find(User.class, user.getId()).getFullName());
	}

	@Test
	public void deleteUser() throws Exception {
		User user = new User();
		user.setFullName("ambrosio");
		user.setPassword("secreta");
		user.setEmail("delete-user@mail.com");

		tx.begin();
		em.persist(user);
		tx.commit();
		
		assertNotNull(em.find(User.class, user.getId()));
		
		tx.begin();
		em.remove(user);
		tx.commit();
		
		assertNull(em.find(User.class, user.getId()));
	}
}
