package org.soujava.demos.rag;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.logging.Logger;

@Path("/hr/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HRPolicyResource {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyResource.class.getName());
    @Inject
    private HRPolicyService service;

    @POST
    @Path("/ask")
    public AskHRPolicyResponse ask(@Valid AskHRPolicyRequest request) {
        LOGGER.info("Received request: " + request);
        return service.ask(request);
    }
}