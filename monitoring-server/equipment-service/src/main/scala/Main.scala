import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import controllers._

object Main extends TwitterServer {
    import startup.Startup._

    startup()

    val server =
        Http.serve(
          ":8081",
          ModelsController.api
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
