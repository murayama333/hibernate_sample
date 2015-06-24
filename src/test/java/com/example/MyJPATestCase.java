package com.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

public class MyJPATestCase {

	EntityManagerFactory emf;
	EntityManager em;

	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("jpa-sample");
		em = emf.createEntityManager();
	}

	@After
	public void tearDown() {
		em.close();
		emf.close();
	}	
}
