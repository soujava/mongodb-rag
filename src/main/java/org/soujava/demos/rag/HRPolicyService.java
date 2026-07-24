package org.soujava.demos.rag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class HRPolicyService {

    private static final Logger LOGGER = Logger.getLogger(HRPolicyService.class.getName());

    @Inject
    private HRPolicyAgent agent;

    public AskHRPolicyResponse ask(AskHRPolicyRequest request) {
        var answer = agent.ask(request.question());
        var response = new AskHRPolicyResponse(request.question(), answer);
        LOGGER.info("Generated response: " + response);
        return response;
    }
}