package org.soujava.demos.rag;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface HRPolicyAgent {

    @SystemMessage("""
            You are an assistant responsible for answering questions about
            the company's Human Resources policies.

            Answer using only the information retrieved from the HR policy
            knowledge base.

            If the retrieved information does not contain the answer, say:
            "The available HR policies do not contain this information."

            Do not invent policies, benefits, limits, dates, or approvals.
            Keep the answer concise and clear.
            """)
    String ask(@UserMessage String question);
}
