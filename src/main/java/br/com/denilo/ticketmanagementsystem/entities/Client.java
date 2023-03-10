package br.com.denilo.ticketmanagementsystem.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "client")
public class Client extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Ticket> ticketList = new ArrayList<>();

    public Client() {
    }

    public Client(String name, String cpf, String email, String password) {
        super(name, cpf, email, password);
    }

    public Client(Long id, String name, String cpf, String email, String password) {
        super(id, name, cpf, email, password);
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void addTicket(Ticket ticket) {
        this.ticketList.add(ticket);
    }
}
