package br.com.denilo.ticketmanagementsystem.services;

import br.com.denilo.ticketmanagementsystem.dtos.ClientDTO;
import br.com.denilo.ticketmanagementsystem.entities.Client;
import br.com.denilo.ticketmanagementsystem.repositories.ClientRepository;
import br.com.denilo.ticketmanagementsystem.repositories.TicketRepository;
import br.com.denilo.ticketmanagementsystem.services.exceptions.DataIntegrityErrorException;
import br.com.denilo.ticketmanagementsystem.services.exceptions.ResourceNotFoundException;
import br.com.denilo.ticketmanagementsystem.services.util.UserValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public ClientDTO findById(Long id) {
        return toClientDTO(clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with ID " + id + " does not exist.")));
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll()
                .stream()
                .map(x -> toClientDTO(x))
                .toList();
    }

    public ClientDTO create(@Valid ClientDTO clientDTO) {
        UserValidation.userValidationForUserExist(clientDTO.getCpf(), clientDTO.getEmail());
        return toClientDTO(clientRepository.save(convertToClient(clientDTO)));
    }

    public ClientDTO update(Long id, @Valid ClientDTO clientDTO) {
        clientDTO.setId(id);
        UserValidation.userValidationForUserUpdate(clientDTO.getId(), clientDTO.getCpf(), clientDTO.getEmail());
        Client clientData = convertToClientWithId(clientDTO);
        Client clientUpdate = clientRepository.findById(clientDTO.getId()).get();
        Client updatedClient = updateData(clientData, clientUpdate);
        clientRepository.save(updatedClient);
        return toClientDTO(updatedClient);
    }

    public void delete(Long id) {
        this.findById(id);
        Optional<Client> client = clientRepository.findById(id);
        if (client.get().getTicketList().size() > 0) {
            throw new DataIntegrityErrorException("Client has tickets, it cannot be deleted.");
        }
        clientRepository.deleteById(id);
    }

    private ClientDTO toClientDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(client.getName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setCpf(client.getCpf());
        clientDTO.setCreationDate(client.getCreationDate());
        return clientDTO;
    }

    private Client convertToClient(@Valid ClientDTO clientDTO) {
        return new Client(
                clientDTO.getName(),
                clientDTO.getCpf(),
                clientDTO.getEmail(),
                clientDTO.getPassword()
        );
    }

    private Client convertToClientWithId(@Valid ClientDTO clientDTO) {
        return new Client(
                clientDTO.getId(),
                clientDTO.getName(),
                clientDTO.getCpf(),
                clientDTO.getEmail(),
                clientDTO.getPassword()
        );
    }

    private Client updateData(Client clientData, Client clientUpdate) {
        clientUpdate.setName(clientData.getName());
        clientUpdate.setCpf(clientData.getCpf());
        clientUpdate.setEmail(clientData.getEmail());
        clientUpdate.setPassword(clientData.getPassword());
        return clientUpdate;
    }

}