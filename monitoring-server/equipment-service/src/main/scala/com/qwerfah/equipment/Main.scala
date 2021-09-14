import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import com.qwerfah.equipment.controllers._
import com.qwerfah.equipment.Startup

object Main extends TwitterServer {
    import Startup._

    startup()

    val server =
        Http.serve(
          ":8081",
          ModelsController.api
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
