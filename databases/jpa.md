# JPA (Java Persistence API)

It is a relatively new way to work with databases in Java. It makes it easy to work with object-oriented data.

JPA is object-relation mapping(ORM) specification that makes it easier to convert between business objects and relational database of an application. It can automatically create DB tables based on relationships between business objects. JPA runs on top of JDBC.

When working with JPA, business objects are known as entities and are managed by an entity manager.

To turn a normal business class into an entity, you code annotations in the class. These annotations specify how the class should be stored in database and also specify how one class relates to another. These are POJOs.

## Configuration of IDE

You need to include MySQL JDBC Driver library into project. You need to include JPA library into project like EclipseLink or Hibernate. Then, you need to configure persistence unit that tells JPA  how to connect to a database.
For EclipseLink library - use file named persistence.xml to configure.

persistence.xml file

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence version="2.1"
              xmlns="http://xmlns.jcp.org/xml/ns/persistence"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
              http://xmlns.jcp.org/xml/ns/persistence/persistence_2.1.xsd">
  <persistence-unit name="emailListPU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <exclude-unlisted-classes>false</excluse-unlisted-classes>
    <properties>
      <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test_jpa" />
      <property name="javax.persistence.jdbc.user" value="root" />
      <property name="javax.persistence.jdbc.password" value="password" />
      <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
      <property name="javax.persistence.schema-generation.database.action" value="create" />
    </properties>
  </persistence-unit>
</persistence>
```

|Element  | Description |
----------|:------------:|
|persistence-unit | name attribute specifies the name you use in your code to get a refernce to the database
The transaction-type attribute specifies how the application works with entity managers. RESOURCE_LOCAL specifies that you will create and manage entity managers yourself. |
|provider |full class name of JPA PersistenceProvider class |
|excelude-unlisted-classes |false value means that JPA uses all classes annotated as entities. Otherwise, you have to list each class you want JPA to use as an entity. |
|shared-cache-mode | determines the caching strategy used by JPA. Caching can improve performance. |

- property element with name of schema-generation.database.action specifies what JPA should do when it encounters missing tables.

## How to code JPA entities:

It is a business class with annotations added to it.
First, you add @Entity annotation immediately before the class declaration itself. It tells JPA that this class defines an entity that should be stored in a database.
Second, add @Id annotation. Tells JPA that following field is the primary key for the entity.
Third, to generate values automatically for the primary key, you add @GeneratedValue annotation. This includes strategy attribute which tells JPA how to generate the primary key. The AUTO strategy lets JPA determine how to generate primary key.

By default, JPA uses same name for the table in database as the name of the class. To override default table name, use @Table annotation immediately after @Entity annotation.

`@Table(name="Customer")`

By default JPA uses same names for columns in database as the names of the fields in the class. If you want to override these names, use @Column annotation with name attribute.

```Java
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long userId;
  private String firstName;
  private String lastName;
  private String email;

  public Long getUserId() {
    return userId;
  }

  // other setter and getter methods.
}
```

- When you use getter annotations, JPA uses the get and set methods of class to get and set the fields. When you use field annotations, JPA uses reflection to get and set the field values. So, if you need to run code in your get or set methods, you should use getter annotations, not field annotations. You cannot mix field and getter annotations in the same class.

- Code that uses getter annotations

```java
private Long userId;

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
public Long getUserId() {
  return userId;
}

public void setUserId(Long userId) {
  this.userId = userId;
}
```

- Code that uses field annotations

```Java
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Long userId;

public Long getUserId() {
  return userId;
}

public void setUserId(Long userId) {
  this.userId = userId;
}

```

- When you code a query in JPA, it automatically performs any joins necessary to satisfy the relationships between entities. When you save an entity, JPA automatically saves any dependent entities. To make these relationships work, we use annotations in entity classes.
- You can code One to many or many to one relationship using @OneToMany or @ManyToOne annotations.
- The fetch element determines when JPA loads the line items for an invoice. By default, JPA uses lazy loading, means JPA first gets the invoice from the database, its line items are empty. Then JPA fetches these line items the first time you attemp to acces the line items in this invoice. Alternatively, you can specify fetch element to FetchType.EAGER.
- Eager loading may take longer to get the initial entity, but should run faster after that. Lazy loading gets the initial entity faster, but may run more slowly later.If the list contained by the entity is likely to be small or entity is not useful without the list, you should use eager fetching.
- The cascade element tells JPA how to handle the line items when it updates an invoice. CascadeType.ALL means apply any updates to the invoice to the line items too.


```Java
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.peristence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class Invoice implements Serializable {
  @ManyToOne
  private User user;

  @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
  private List<LineItem> lineItems;

  @Temporal(javax.persistence.TemporalType.DATE)
  private Date invoiceDate;

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long invoiceNumber;

  private boolean isProcessed;

  // getters and setters for fields
}
```

  - **cascade**: CascadeType.ALL - all operations that change the invoice should also update all line items.
              CascadeType.PERSIST - any time a new invoice is inserted into the database, any items it has should also be inserted.
              CascadeType.MERGE - any time an invoice is updated, any changes to its line items should also be updated.
              CascadeType.REMOVE - any time an invoice is removed from the database, all of its line items should also be removed.


- To work with Date and Timestamp of SQL, we can use @Temporal annotation. In @Temporal annotation, we can have one of three values.
TemporalType.DATE - stores only date
TemporalType.TIME - stores only time
TemporalType.TIMSTAMP - stores both date and time.
Mostly, we use TIMESTAMP option and then we omit the part that we don't need.

## EntityManager and its management

When using full Java EE server, JPA provides a class named EntityManagerFactory that you can use to get entity managers. Entity manager factories are thread safe. However, entity managers are not. So, you request for a new entity manager for each method and then you return it.

You can create a class to get entity managers like this.

```java
import javax.persistence.EntityManagerFactory;
import javax.persistence.Peristence;

public class DBUtil {
  private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("emailListPU"); // this name refers to persistence-unit name in xml file.

  public static EntityManagerFactory getEmFactory() {
    return emf;
  }
}
```

- How to retrieve entity by primary key

```java
import javax.persistence.EntityManager;

public class UserDB {
  public static User getUserById(long userId) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    try {
      User user = em.find(User.class, userId);
      return user;
    }
    finally {
      em.close();
    }
  }
}
```

## JPQL (Java Persistence Query Language)

It is very similar to SQL statements. It is an object-oriented query language.In some JPA implementation, getResultList() method returns null and in some it returns empty list. So, we need to check both. How to retrieve multiple entities?

```java
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class InvoiceDB {
  public static List<Invoice> selectUnprocessedInvoices() {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();

    // This is JPQL, select i from invoice table alias as i where condition.
    String qString = "SELECT i FROM Invoice i WHERE i.isProcessed = 'n'";

    TypedQuery<Invoice> q = em.createQuery(qString, Invoice.class); // returns objects of invoice class second argument.

    List<Invoice> invoices;

    try {
      invoices = q.getResultList();   // returns list of Invoices.
      if(invoices == null || invoices.isEmpty())  // first check if null and then execute method to avoid null pointer exception
        invoices = null;
    }
    finally {
      em.close();
    }
  }
}
```

- How to retrieve a single entity
Named parameters are specified using colon(:)
merge(entity) method updates an entity in the database and returns an attached entity.

```java
import javax.persistence.EntityManager;
import javax.persistence.NOResultException;
import javax.persistence.TypedQuery;

public class UserDB {
  public static User selectUser(String email) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    String qString = "SELECT u FROM User u WHERE u.email = :email";   // here :email specifies a parameter for the query.

    TypedQuery<User> q = em.createQuery(qString, User.class);
    q.setParameter("email", email);   // set parameter for the query email to email.

    User user = null;

    try {
      user = q.getSingleResult();   // getSingleResult() returns only single result
    } catch(NOResultException e) {
      System.out.println(e);
    } finally {
      em.close();
    }
    return user;
  }

  public static void insert(User user) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    EntityTransaction trans = em.getTransaction();
    trans.begin();

    try {
      em.persist(user);
      trans.commit();
    } catch (Execption e ) {
      System.out.println(e);
      trans.rollback();
    } finally {
      em.close();
    }
  }

  public static void update(User user) {
    EntityManager em = DBUtil.getEmFactory().crateEntityManager();
    EntityTransaction trans = em.getTransaction();

    trans.begin();

    try {
      em.merge(user);
      trans.commit();
    } catch (Exception e) {
      System.out.println(e);
      trans.rollback();
    } finally {
      em.close();
    }
  }

  public static void delete(User user) {
    EntityManager em = DBUtil.getEmFactory().createEntityManager();
    EntityTransaction trans = em.getTransaction();

    trans.begin();

    try {
      em.remove(em.merge(user));
      trans.commit();
    } catch(Exception e) {
      System.out.println(e);
      trans.rollback();
    } finally {
      em.close();
    }
  }

  public static boolean emailExists(String email) {
    User u = selectUser(email);
    return u != null; // if user found, return true
  }
}
```

## Insert, update and delete an entity

Modifying a table requires you to go through a transaction. You need to commit the operations to the database at the end or if it fails, you need to rollback. You begin a transaction by calling begin() method. To insert a new entity, you use persist() method. To remove an entity, you use remove() method.

**Update multiple entities**

```java
EntityTransaction trans = em.getTransaction();
String qString = "UPDATE Invoice i SET i.isProcessed = 'y' WHERE i.id < :id";

Query q = em.createQuery(qString);
q.setParameter(id, 200);
int count = 0;
trans.begin();

try {
  count = q.executeUpdate();
  trans.commit();
} catch(Exception e) {
  trans.rollback();
} finally {
  em.close();
}
```

**delete multiple entities**

```java
EntityTransaction trans = em.getTransaction();
String qString = "DELETE FROM Invoice i WHERE i.id < :id";
Query q = em.createQuery(qString);
q.setParameter(id, 200);
int count = 0;

try {
  trans.begin();
  count = q.executeUpdate();
  trans.commit();
} catch (Exception e) {
  trans.rollback();
} finally {
  em.close();
}
```

- executeUpdate() method returns a count of number of entities affected by the query.
- These queries may trigger automatic updates or deletions. For example, deleting invoice may delete all its line items automatically.

- How to insert an entity (Employee is an entity with four fields, persistence-unit name is eclipselink_JPA)

```java
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CreateEmployee {
  public static void main(String[] args) {
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("eclipselink_JPA");
    EntityManager entityManager = emFactory.createEntityManager();

    EntityTransaction transaction = entityManager.getTrasaction();
    try {
      transaction.begin();

      Employee employee = new Employee();
      employee.setEid = 1201;
      employee.setName("Piyush");
      employee.setSalary(20000);
      employee.setDesignation("Electrical Engineer");

      entityManager.persist(employee);
      entityManager.getTrasaction().commit();
    } catch(Exception e) {
      transaction.rollback();
    } finally {
      entityManager.close();
    }
    emFactory.close();
  }
}
```

- Find an entity

```Java
public class FindEmployee {
  public static void main(String[] args) {
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("eclipselink_JPA");
    EntityManager em = emFacotry.createEntityManager();
    Employee employee = em.find(Employee.class, 1201);

    System.out.println("Employee ID = " employee.getId());
    System.out.println("Employee Name = " employee.getName());
  }
}
```

- Update entity

```java
public class UpdateEmployee {
  public static void main(String[] args) {
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("eclipselink_JPA");
    EntityManager em = emFactory.createEntityManager();
    entityManager.getTransaction().begin();

    Employee employee = entityManager.find(Employee.class, 1201);

    System.out.println(employee);
    employee.setSalary(46000);    // update salary
    em.getTrasaction().commit();

    System.out.println(employee);
    em.close();
    emFactory.close();
  }
}
```

- Delete entity

```Java
public class DeleteEmployee {
  public static void main(String[] args) {
    EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("eclipselink_JPA");
    EntityManager em = emFactory.createEntityManager();
    em.getTrasaction().begin();

    Employee employee = em.find(Employee.class, 1201);
    em.remove(employee);
    em.getTrasaction().commit();
    em.close();
    emFactory.close();
  }
}
```
