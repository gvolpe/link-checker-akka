package com.gvolpe.linkchecker

import java.util.concurrent.Executor
import scala.concurrent.Future

object FakeWebClient extends WebClient {

  val firstLink = "http://rkuhn.info/1"

  val bodies = Map(
    firstLink ->
      """
        |<html>
        | <head><title>Page 1</title></head>
        | <body>
        |   <h1>A Link</h1>
        |   <a href="http://rkuhn.info/2">Click here</a>
        | </body>
        |</html>
      """.stripMargin
  )

  val links = Map(
    firstLink -> Seq("http://rkuhn.info/2")
  )

  def get(url: String)(implicit exec: Executor): Future[String] =
    bodies get url match {
      case None       => Future.failed(BadStatus(404))
      case Some(body) => Future.successful(body)
    }

}
