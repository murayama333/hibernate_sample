# Hibernate


## Hibernateとは

HibernateはJavaのORMフレームワークです。ORMとはObject Relational Mappingの略でJavaの世界のオブジェクトとリレーショナルデータベースの世界のレコードを関連付けるものです。

Hibernateはオープンソースです。以下のサイトで公開されています。

> http://hibernate.org/

## HibernateとJPA

Java EEにはリレーショナルデータベースを扱うフレームワークとして、JPA（Java Persistence API）が存在します。

HibernateはJPAが策定される以前からオープンソースとして開発されていました。当時、SQLのコーディングに苦労していたJavaエンジニアにとって、Hibernateの登場はかなりの注目を集めました。

やがてHibernateのようなORMフレームワークが開発の現場で認知し始め、その必要性が高まったことで、JCPはJPAの仕様策定に取りかかります。

ServletやJSPと同様に、JPAは仕様です。JPAを実際に動かすには参照実装（RI）が必要です。JPAの参照実装にはいくつかの選択肢があります。

+ EclipseLink
+ GlassFish
+ Hibernate

> HibernateはJPAの先駆けのフレームワークでありながら、JPAの仕様も満たしているのです。

ここではJPAの仕様に従ってHibernateを操作する手順を学習します。

## JPAの主要なコンポーネント

JPAの主要なコンポーネントは以下のとおりです。

+ Entity
+ EntityManager
+ JPQL
+ persistence.xml


### Entity

Entityはリレーショナルデータベース上のテーブルと対になるクラスです。またEntityクラスには@Entityアノテーションを付与し、テーブルのカラムをクラスのプロパティとして定義します。


```
package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="Book.findAll", query="SELECT e FROM Book e")
public class Book {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    private int price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
```

### EntityManager

EntityManagerはEntityの永続化を管理します。EntityManagerインスタンスはEntityManagerFactoryクラスから生成します。

```
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-sample");
EntityManager em = emf.createEntityManager();
```

EntityManagerを通じてEntityを取得する場合は次のように実装します。

```
Book book1 = em.find(Book.class, 1l);
```

上記の場合、IDが1のBookエンティティを取得しています。


またEntityを保存する場合は次のように実装します。

```
EntityTransaction tx = em.getTransaction();

Book book1 = new Book();
book1.setTitle("Spring book");
book1.setPrice(3000);

tx.begin();
em.persist(book1);
tx.commit();
```

またEntityManager、およびEntityManagerFactoryは終了時にクローズするようにします。


```
em.close();
emf.close();
```

> EntityManagerを使えばEntityの操作を通じてリレーショナルデータベースを参照したり、更新したりすることができます。ここまでSQLを記述していないことに注意してください。


### JPQL

JPQL（Java Persistence Query Language）はSQLライクなクエリ言語です。リレーショナルデータベースのテーブルに対する操作ではなく、エンティティに対する操作と考えると良いでしょう。

JPQLは次のような構文になります。

```
"SELECT e FROM Book e"
```

JPQLはEntityに対して@NamedQueryアノテーションで定義できます。

```
@Entity
@NamedQuery(name="Book.findAll", query="SELECT e FROM Book e")
public class Book {
```

JPQLはEntityManagerに対して次のように実行します。

```
Query query = em.createNamedQuery("Book.findAll");
List<Book> books = (List<Book>) query.getResultList();
```

### persistence.xml

persistence.xmlはJPAの設定ファイルです。データベースの接続情報やEntityクラスの定義など様々な情報を定義します。

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
	xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

	<persistence-unit name="jpa-sample">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<class>com.example.Book</class>
		<class>com.example.Article</class>
		<class>com.example.Comment</class>
		<properties>
			<property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
			<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost/mydb" />
			<property name="javax.persistence.jdbc.user" value="root" />
			<property name="javax.persistence.jdbc.password" value="" />
			<property name="hibernate.hbm2ddl.auto" value="create-drop" />
			<property name="hibernate.show_sql" value="false" />
		</properties>
	</persistence-unit>
</persistence>
```
