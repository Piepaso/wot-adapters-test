import it.wldt.core.engine.DigitalTwin
import it.wldt.core.engine.DigitalTwinEngine
import it.wldt.core.model.ShadowingFunction
import org.eclipse.thingweb.Servient
import org.eclipse.thingweb.binding.mqtt.MqttClientConfig
import org.eclipse.thingweb.binding.mqtt.MqttProtocolClientFactory
import org.eclipse.thingweb.binding.http.HttpProtocolClientFactory

import kotlinx.coroutines.*
import org.eclipse.thingweb.binding.http.HttpProtocolServer
import org.eclipse.thingweb.binding.mqtt.MqttProtocolServer
import org.slf4j.LoggerFactory
import wotDigitalAdapter.*
import physical.*


fun main() {
    val N = 0
    val logger = LoggerFactory.getLogger("AppMain")
    val mqttExposerConfig = MqttClientConfig("localhost", 61890, "exposer-${N}")
    val port = 8080
    val baseUrl = "http://localhost:${port}"

    val exposingServient = Servient(
        servers = listOf(
            HttpProtocolServer(bindPort = port, baseUrls = listOf(baseUrl)),
            MqttProtocolServer(mqttExposerConfig)
        )
    )

    val woTDigitalAdapter = WoTDigitalAdapter(
        "digital-adapter-${N}",
        WoTDigitalAdapterConfiguration(
            servient = exposingServient,
            thingTitle = "this is the thing title",
            description = "this is the thing description",
            observableProperties = setOf("temperature-property-key")
        )
    )

    val demoPhysicalAdapter = DemoPhysicalAdapter("demo-physical-adapter-${N}")

    val demoShadowing: ShadowingFunction = DemoShadowingFunction("shadowing-${N}")

    val engine = DigitalTwinEngine()

    val dtDemo = DigitalTwin("dtDemo-${N}", demoShadowing)


    dtDemo.addPhysicalAdapter(demoPhysicalAdapter)
    dtDemo.addDigitalAdapter(woTDigitalAdapter)

    engine.addDigitalTwin(dtDemo)

    runBlocking {
        try {
            logger.info("Starting test...")

            engine.startAll()

            delay(6000) //wait for everything to be properly started

            val thingUrl = "${baseUrl}/${woTDigitalAdapter.thingId}"
            print("\n\n\nthingUrl: $thingUrl\n\n\n")

        } catch (e: Exception) {
            logger.error("An error occurred in main: ${e.message}", e)
        }
    }
}

