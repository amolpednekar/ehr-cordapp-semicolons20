package com.template.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.transactions.LedgerTransaction

class EhrContract : Contract {

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    override fun verify(tx: LedgerTransaction) {
        // Verification logic goes here.
    }

    // Used to indicate the transaction's intent.
    interface Commands : CommandData {
        class CreateEhrCommand : Commands
    }
}