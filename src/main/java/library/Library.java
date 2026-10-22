
package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> books = new ArrayList<>();
    private List<Copy> copies = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();

    public void registerBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (!books.contains(book)) {
            books.add(book);
        }
    }

    public void registerCopy(Copy copy) {
        if (copy == null) {
            throw new IllegalArgumentException("Copy cannot be null");
        }

        copies.add(copy);
    }

    public void registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }

        members.add(member);
    }

    public Loan lend(String memberNumber, String inventoryNumber,
                     LocalDate today) {

        Member member = findMember(memberNumber);
        Copy copy = findCopy(inventoryNumber);

        if (!copy.isAvailable()) {
            throw new IllegalStateException("Copy is not available");
        }

        long openLoans = loans.stream()
                .filter(loan -> loan.getMember().getNumber()
                        .equals(memberNumber))
                .filter(Loan::isOpen)
                .count();

        if (openLoans >= 5) {
            throw new IllegalStateException(
                    "Member already has 5 open loans");
        }

        Loan loan = new Loan(member, copy, today);
        loans.add(loan);
        copy.setAvailable(false);

        return loan;
    }

    public Loan returnCopy(String inventoryNumber, LocalDate today) {
        Copy copy = findCopy(inventoryNumber);

        for (Loan loan : loans) {
            if (loan.getCopy().getInventoryNumber()
                    .equals(inventoryNumber) && loan.isOpen()) {

                loan.setReturnDate(today);
                copy.setAvailable(true);

                return loan;
            }
        }

        throw new IllegalStateException(
                "No open loan for this copy");
    }

    private Member findMember(String number) {
        for (Member member : members) {
            if (member.getNumber().equals(number)) {
                return member;
            }
        }

        throw new IllegalArgumentException(
                "Unknown member: " + number);
    }

    private Copy findCopy(String inventoryNumber) {
        for (Copy copy : copies) {
            if (copy.getInventoryNumber().equals(inventoryNumber)) {
                return copy;
            }
        }

        throw new IllegalArgumentException(
                "Unknown copy: " + inventoryNumber);
    }

    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }

    public int getBookCount() {
        return books.size();
    }
}
