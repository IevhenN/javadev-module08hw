package org.hw;

import org.hw.dao.ClientService;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();

        clientService.create("Nesterenko Ievhen");
        clientService.getById(51L);
        clientService.setName(51L, "Petrenko LTD");

        try {
            clientService.deleteById(201L);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clientService.listAll();
    }
}
