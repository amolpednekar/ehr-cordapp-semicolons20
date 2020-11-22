package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.template.contracts.EhrContract
import com.template.states.EhrState
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker


@InitiatingFlow
@StartableByRPC
class CreateEhrFlowInitiator : FlowLogic<SignedTransaction>() {

    override val progressTracker: ProgressTracker = tracker()

    companion object {
        object CREATING : ProgressTracker.Step("Creating a new EHR")
        object SIGNING : ProgressTracker.Step("Signing the EHR")
        object VERIFYING : ProgressTracker.Step("Verifying the EHR")
        object FINALISING : ProgressTracker.Step("Recording the EHR") {
            override fun childProgressTracker() = FinalityFlow.tracker()
        }

        fun tracker() = ProgressTracker(CREATING, SIGNING, VERIFYING, FINALISING)
    }

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep = CREATING

        val ourIdentity = serviceHub.myInfo.legalIdentities.first()

        val notary = serviceHub.networkMapCache.notaryIdentities.single()

        val state = EhrState("Data", listOf(ourIdentity))
        val utx = TransactionBuilder(notary = notary).addOutputState(state).addCommand(EhrContract.Commands.CreateEhrCommand(), listOf(this.ourIdentity.owningKey))

        progressTracker.currentStep = SIGNING
        val stx = serviceHub.signInitialTransaction(utx)

        progressTracker.currentStep = VERIFYING
        stx.verify(serviceHub)

        progressTracker.currentStep = FINALISING
        return subFlow(FinalityFlow(stx, listOf(), FINALISING.childProgressTracker()))
    }
}

@InitiatedBy(CreateEhrFlowInitiator::class)
class CreateEhrFlowResponder(private val counterpartySession: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call(): SignedTransaction {
        return subFlow(ReceiveFinalityFlow(counterpartySession))
    }
}
