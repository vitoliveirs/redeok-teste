package com.example.controller;

import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.*;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import com.example.dto.ClienteDTO;
import com.example.model.ClienteModel;
import com.example.service.ClienteService;

@Path("/cliente")
@ApplicationScoped
public class ClienteController {

    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /*
     * Cadastra um cliente
     */
    @POST
    @Path("/cadastrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarCliente(ClienteModel cliente) {
        try {
            ClienteDTO dto = clienteService.cadastrarCliente(cliente);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    /*
     * Edita parcialmente um cliente
     */
    @PUT
    @Path("/{documento}")
    public Response editarCliente(@QueryParam("documento") @Parameter(required = true) String documento, ClienteModel cliente) {
        try {
            String result = clienteService.editarCliente(documento, cliente);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    /*
     * Lista todos os clientes
     */
    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarClientes(
        @QueryParam("nome") String nome,
        @QueryParam("dataCriacao") String dataCriacao,
        @QueryParam("tamanhoPagina") int tamanhoPagina,
        @QueryParam("numeroPagina") int numeroPagina) {
        try {
            if (tamanhoPagina == 0) {
                tamanhoPagina = 10; // tamanho padrão
            } 
            List<ClienteDTO> dto = clienteService.listarClientes(nome, dataCriacao, tamanhoPagina, numeroPagina);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    /*
     * Busca apenas um cliente
     */
    @GET
    @Path("/{documento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarCliente(String documento) {
        try {
            ClienteDTO dto = clienteService.buscarCliente(documento);
            return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }
}
