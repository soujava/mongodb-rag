package org.soujava.demos.rag;


import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("/hr/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HRPolicyResource {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyResource.class.getName());

    @Inject
    private HRPolicyService service;

    @Inject
    private HRPolicyContextService contextService;

    @POST
    @Path("/ask")
    public HRPolicyAnswer ask(@Valid HRPolicyQuestion request) {
        LOGGER.info("Received request: " + request);
        return service.ask(request);
    }

    @POST
    @Path("/context")
    public Response addContext(@Valid HRPolicyContextRequest request) {

        HRPolicyContextResponse response =
                contextService.add(request);

        if (response.inserted()) {
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        }

        return Response.ok(response).build();
    }
}