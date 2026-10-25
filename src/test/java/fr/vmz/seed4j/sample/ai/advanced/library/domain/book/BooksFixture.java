package fr.vmz.seed4j.sample.ai.advanced.library.domain.book;

public final class BooksFixture {

  public static final Isbn EFFECTIVE_JAVA = new Isbn("9780134685991");
  public static final Isbn DDD = new Isbn("9780321125217");

  private BooksFixture() {}

  public static Book effectiveJava() {
    return book(EFFECTIVE_JAVA, "Effective Java", "Joshua Bloch");
  }

  public static Book ddd() {
    return book(DDD, "Domain-Driven Design", "Eric Evans");
  }

  public static Book book(Isbn isbn, String title, String author) {
    return new BookToAdd(isbn, new Title(title), new Author(author)).add();
  }
}
