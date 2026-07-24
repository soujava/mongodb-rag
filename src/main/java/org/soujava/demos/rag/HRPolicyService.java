package org.soujava.demos.rag;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HRPolicyService {

    @Inject
    HRPolicyAgent agent;

    public AskHRPolicyResponse ask(AskHRPolicyRequest request) {
        String answer = agent.ask(request.question());
        return new AskHRPolicyResponse(request.question(), answer);
    }
}