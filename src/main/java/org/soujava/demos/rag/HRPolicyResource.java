package org.soujava.demos.rag;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hr/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HRPolicyResource {

    @Inject
    HRPolicyService service;

    @POST
    @Path("/ask")
    public AskHRPolicyResponse ask(@Valid AskHRPolicyRequest request) {
        return service.ask(request);
    }
}