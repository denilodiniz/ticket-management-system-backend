package br.com.denilo.ticketmanagementsystem.services;

import br.com.denilo.ticketmanagementsystem.dtos.TicketDTO;
import br.com.denilo.ticketmanagementsystem.entities.Ticket;
import br.com.denilo.ticketmanagementsystem.repositories.ClientRepository;
import br.com.denilo.ticketmanagementsystem.repositories.TechnicianRepository;
import br.com.denilo.ticketmanagementsystem.repositories.TicketRepository;
import br.com.denilo.ticketmanagementsystem.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TechnicianRepository technicianRepository;

    public TicketDTO findById(Long id) {
        return toTicketDTO(ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID " + id + " does not exist.")));
    }

    public List<TicketDTO> findAll() {
        return ticketRepository.findAll()
                .stream()
                .map(this::toTicketDTO)
                .toList();
    }

    public TicketDTO create(TicketDTO ticketDTO) {
        return toTicketDTO(ticketRepository.save(convertToTicked(ticketDTO)));
    }

    private Ticket convertToTicked(TicketDTO ticketDTO) {
        return new Ticket(
          ticketDTO.getTitle(),
          ticketDTO.getObservations(),
          ticketDTO.getPriority(),
          clientRepository.findById(ticketDTO.getClientId()).get(),
          technicianRepository.findById(ticketDTO.getTechnicianId()).get()
        );
    }

    private TicketDTO toTicketDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getCreationDate(),
                ticket.getClosedDate(),
                ticket.getTitle(),
                ticket.getObservations(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getClient().getId(),
                ticket.getTechnician().getId(),
                ticket.getClient().getName(),
                ticket.getTechnician().getName()
        );
    }

}