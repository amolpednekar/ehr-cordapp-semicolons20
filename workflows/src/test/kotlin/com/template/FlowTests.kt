package com.template

import com.template.flows.CreateEhrFlowInitiator
import com.template.flows.Responder
import com.template.states.EhrState
import net.corda.testing.node.MockNetwork
import net.corda.testing.node.MockNetworkParameters
import net.corda.testing.node.TestCordapp
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class FlowTests {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val network = MockNetwork(MockNetworkParameters(cordappsForAllNodes = listOf(
        TestCordapp.findCordapp("com.template.contracts"),
        TestCordapp.findCordapp("com.template.flows")
    )))
    private val goaHospital = network.createNode()
    private val puneHospital = network.createNode()

    init {
        listOf(goaHospital, puneHospital).forEach {
            it.registerInitiatedFlow(Responder::class.java)
        }
    }

    @Before
    fun setup() = network.runNetwork()

    @After
    fun tearDown() = network.stopNodes()

    @Test
    fun `create a EHR`() {
        val flow = CreateEhrFlowInitiator()
        goaHospital.startFlow(flow).toCompletableFuture()
        network.runNetwork()
        val result = goaHospital.services.vaultService.queryBy(contractStateType = EhrState::class.java).states
        logger.info("Create EHR: $result")
    }
}