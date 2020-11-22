package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.flows.*
import net.corda.core.utilities.ProgressTracker


@InitiatingFlow
@StartableByRPC
class CreateEhrFlowInitiator : FlowLogic<Unit>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() {
        // Initiator flow logic goes here.
    }
}

@InitiatedBy(CreateEhrFlowInitiator::class)
class CreateEhrFlowResponder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Responder flow logic goes here.
    }
}
