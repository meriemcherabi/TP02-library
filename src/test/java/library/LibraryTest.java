
package library;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class LibraryTest {

    @Test
    void newCopyIsAvailable() {
        Book book = new Book("ISBN-1", "Java", 2024);
        Copy copy = new Copy("C-001", book);

        assertTrue(copy.isAvailable());
    }

    @Test
    void lendingMakesCopyUnavailable() {
        Library library = new Library();
        Book book = new Book("ISBN-1", "Java", 2024);
        Copy copy = new Copy("C-001", book);
        Member member = new Member("M-001", "Sara");

        library.registerBook(book);
        library.registerCopy(copy);
        library.registerMember(member);

        library.lend("M-001", "C-001", LocalDate.of(2026, 10, 22));

        assertFalse(copy.isAvailable());
    }

    @Test
    void returningMakesCopyAvailableAgain() {
        Library library = new Library();
        Book book = new Book("ISBN-1", "Java", 2024);
        Copy copy = new Copy("C-001", book);
        Member member = new Member("M-001", "Sara");

        library.registerCopy(copy);
        library.registerMember(member);

        LocalDate today = LocalDate.of(2026, 10, 22);
        library.lend("M-001", "C-001", today);
        library.returnCopy("C-001", today.plusDays(5));

        assertTrue(copy.isAvailable());
    }

    @Test
    void loanDueDateIsFourteenDaysLater() {
        Member member = new Member("M-001", "Sara");
        Book book = new Book("ISBN-1", "Java", 2024);
        Copy copy = new Copy("C-001", book);
        LocalDate start = LocalDate.of(2026, 10, 22);

        Loan loan = new Loan(member, copy, start);

        assertEquals(start.plusDays(14), loan.getDueDate());
        assertTrue(loan.isOpen());
    }
}
