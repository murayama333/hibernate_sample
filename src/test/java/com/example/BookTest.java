package com.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.persistence.EntityTransaction;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class BookTest extends MyJPATestCase{

	@Test
	public void test_find() {
		long id = save(javaBook());
		Book actual = em.find(Book.class, id);
		assertThat(actual.getTitle(), is("Java book"));
		assertThat(actual.getPrice(), is(2000));
	}

	@Test
	public void test_createNamedQuery() {
		save(javaBook());
		save(phpBook());
		@SuppressWarnings("unchecked")
		List<Book> actual = (List<Book>)em.createNamedQuery("Book.findAll").getResultList();
		assertThat(actual.size(), is(2));
	}

	private Book javaBook() {
		Book book = new Book();
		book.setTitle("Java book");
		book.setPrice(2000);
		return book;
	}

	private Book phpBook() {
		Book book = new Book();
		book.setTitle("PHP book");
		book.setPrice(1000);
		return book;
	}

	private long save(Book book) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(book);
		tx.commit();
		return book.getId();
	}
}
