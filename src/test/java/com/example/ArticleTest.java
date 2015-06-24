package com.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityTransaction;

import org.junit.Test;

public class ArticleTest extends MyJPATestCase {

	@Test
	public void test_ony_to_many() {

		long articleAId = save(articleAWith1Comment());
		long articleBId = save(articleBWith2Comment());

		// Test ArticleA 
		Article actual = em.find(Article.class, articleAId);
		assertThat(actual.getTitle(), is("Title A"));
		assertThat(actual.getBody(), is("Body A"));
		assertThat(actual.getComments().size(), is(1));
	
		// Test ArticleB
		actual = em.find(Article.class, articleBId);
		assertThat(actual.getTitle(), is("Title B"));
		assertThat(actual.getBody(), is("Body B"));
		assertThat(actual.getComments().size(), is(2));
		
//		System.out.println("--" + actual.getTitle() + "--");
//		System.out.println(actual.getBody());
//		actual.getComments().forEach(c ->{
//			System.out.println(">>" + c.getName() + ":" + c.getMessage());
//		});		
	}

	@Test
	public void test_many_to_one() {

		save(articleAWith1Comment());

		Comment nakazyComment = em.find(Comment.class, 1l);

		assertThat(nakazyComment.getArticle().getTitle(), is("Title A"));
		assertThat(nakazyComment.getMessage(), is("awesome article."));	
	}

	private Article articleAWith1Comment() {
		Article article = new Article();
		article.setTitle("Title A");
		article.setBody("Body A");

		Comment comment1 = new Comment();
		comment1.setMessage("awesome article.");
		comment1.setName("Nakazy");
		comment1.setArticle(article);
		article.getComments().add(comment1);

		return article;
	}

	private Article articleBWith2Comment() {
		Article article = new Article();
		article.setTitle("Title B");
		article.setBody("Body B");

		Comment comment1 = new Comment();
		comment1.setMessage("nice article.");
		comment1.setName("Tat");
		comment1.setArticle(article);
		article.getComments().add(comment1);

		Comment comment2 = new Comment();
		comment2.setMessage("good article.");
		comment2.setName("Kid");
		comment2.setArticle(article);
		article.getComments().add(comment2);
		return article;
	}

	private long save(Article article) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(article);
		article.getComments().forEach(em::persist);
		tx.commit();
		return article.getId();
	}

}
