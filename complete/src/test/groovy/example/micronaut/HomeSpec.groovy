package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class HomeSpec extends Specification {

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    private Integer userCounter(String html) {
        String span = "<span id=\"userCounter\">"
        String subHtml = html.substring(html.indexOf(span) + span.length())
        subHtml = subHtml.substring(0, subHtml.indexOf("</span>"))

        try {
            return Integer.valueOf(subHtml)
        } catch(java.lang.NumberFormatException e ){
            return -1
        }
    }

    void "test user counts get updated through websockets"() {

        when:
        PollingConditions conditions = new PollingConditions(timeout: 5)

        then:
        conditions.eventually {
            userCounter(client.toBlocking().retrieve(HttpRequest.GET("/"), String.class)) == 0
        }

        when:
        client.toBlocking().exchange(HttpRequest.POST("/user/register", new User("sherlock", "elementary")))
        client.toBlocking().exchange(HttpRequest.POST("/user/register", new User("watson", "elementary")))
        conditions = new PollingConditions(timeout: 5)

        then:
        conditions.eventually {
            userCounter(client.retrieve(HttpRequest.GET("/"), String.class)) == 2
        }

    }
}
