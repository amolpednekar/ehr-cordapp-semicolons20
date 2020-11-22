package com.template.states

import com.template.contracts.TemplateContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty

@BelongsToContract(TemplateContract::class)
data class EhrState(
        val data: String,
        override val participants: List<AbstractParty>,
        override val linearId: UniqueIdentifier = UniqueIdentifier()) : LinearState
