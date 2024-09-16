package com.example.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import com.example.model.*;
import com.example.dto.*;
import com.example.service.EnderecoService;

@Path("/endereco")
@ApplicationScoped
public class EnderecoController {

    private EnderecoService enderecoService;

     public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @POST
    @Path("/cadastrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
     public Response cadastrarEndereco(EnderecoModel endereco, @QueryParam("documento") @Parameter(required = true) String documento) {
        try {
            EnderecoModel result = enderecoService.cadastrarEndereco(endereco, documento);
                return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }
    
    @DELETE
    public Response deletarEnderecoCliente(@QueryParam("Documento") @Parameter(required = true) String documento,
    @QueryParam("NumeroResidencial") @Parameter(required = true) String numero,
    @QueryParam("cep") @Parameter(required = true) String cep) {
        try {
            boolean dto = enderecoService.deletarEndereco(documento, numero, cep);
                return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{documento}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarEnderecoCliente(String documento) {
        try {
            List<EnderecoDTO> dto = enderecoService.buscarEndereco(documento);
                return Response.ok(dto).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();
        }
    }
}
