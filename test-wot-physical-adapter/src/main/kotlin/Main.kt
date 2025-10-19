import it.wldt.core.engine.DigitalTwin
import it.wldt.core.engine.DigitalTwinEngine
import it.wldt.core.model.ShadowingFunction
import org.eclipse.thingweb.Servient
import org.eclipse.thingweb.binding.mqtt.MqttClientConfig
import org.eclipse.thingweb.binding.mqtt.MqttProtocolClientFactory
import org.eclipse.thingweb.binding.http.HttpProtocolClientFactory

import java.net.URI
import kotlinx.coroutines.*
import org.eclipse.thingweb.binding.http.HttpProtocolServer
import org.eclipse.thingweb.binding.http.HttpsProtocolClientFactory
import org.eclipse.thingweb.binding.mqtt.MqttProtocolServer
import org.slf4j.LoggerFactory
import wotDigitalAdapter.*
import wotPhysicalAdapter.*
import physical.*
import digital.*


fun main() {
    val N = 0
    val logger = LoggerFactory.getLogger("AppMain")
    val mqttConsumerConfig = MqttClientConfig("localhost", 61890, "consumer-${N}")

    val consumingServient = Servient(
        clientFactories = listOf(
            MqttProtocolClientFactory(mqttConsumerConfig), //needs mqtt first
            HttpProtocolClientFactory(),
            HttpsProtocolClientFactory()
        )
    )

    val wotPhysicalAdapter = WoTPhysicalAdapter(
        "wot-physical-adapter-${N}",
        WoTPhysicalAdapterConfiguration(
            tdSource = ThingDescriptionSource.FromUri(URI.create("http://localhost:8080/digital-adapter-${N}")),
            servient = consumingServient,
            defaultPollingOptions = PollingOptions(1000, true)
        )
    )
    val testShadowing: ShadowingFunction = DemoShadowingFunction("shadowing-${N + 1}")

    val engine = DigitalTwinEngine()

    val dtTest = DigitalTwin("dtTest-${N}", testShadowing)



    dtTest.addPhysicalAdapter(wotPhysicalAdapter)
    dtTest.addDigitalAdapter(DemoDigitalAdapter("demo-digital-adapter-${N}"))

    engine.addDigitalTwin(dtTest)

    runBlocking {
        try {
            logger.info("Starting test...")
            engine.startAll()
        } catch (e: Exception) {
            logger.error("An error occurred in main: ${e.message}", e)
        }
    }
}

