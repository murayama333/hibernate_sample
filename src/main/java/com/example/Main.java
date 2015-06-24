package com.example;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Created by murayama on 2014/12/06.
 */
public class Main {

	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-sample");
		EntityManager em = emf.createEntityManager();

		saveBooks(em);
		selectAll(em);
		find(em);

		em.close();
		emf.close();
	}

	private static void find(EntityManager em) {
		
		Book book1 = em.find(Book.class, 1l);
		
		System.out.println(book1.getId());
		System.out.println(book1.getTitle());
		System.out.println(book1.getPrice());
	}

	private static void selectAll(EntityManager em) {
		
		Query query = em.createNamedQuery("Book.findAll");		
		
		@SuppressWarnings("unchecked")
		List<Book> books = (List<Book>) query.getResultList();
		for (Book book : books) {
			System.out.println(book.getId() + "," + book.getTitle() + ","+ book.getPrice());
		}
	}

	private static void saveBooks(EntityManager em) {

		EntityTransaction tx = em.getTransaction();

		Book book1 = new Book();
		book1.setTitle("Spring book");
		book1.setPrice(3000);

		Book book2 = new Book();
		book2.setTitle("Java book");
		book2.setPrice(2000);

		tx.begin();
		em.persist(book1);
		em.persist(book2);
		tx.commit();
	}
}
