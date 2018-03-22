package server

import java.time.LocalDateTime
import javax.inject.Inject

import akka.actor.ActorRef
import akka.stream.Materializer
import pme123.adapters.server.control.demo.DemoService.toISODateTimeString
import pme123.adapters.server.control.{JobProcess, LogService}
import pme123.adapters.shared.LogLevel.{DEBUG, ERROR, INFO, WARN}
import pme123.adapters.shared._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class WizardDemoProcess @Inject()()
                                            (implicit val mat: Materializer, val ec: ExecutionContext)
  extends JobProcess {

  val jobLabel = "WizardDemo Job"

  def createInfo(): ProjectInfo = // check createInfo for adding more infos!
    createInfo(version.BuildInfo.version)

  // the process fakes some long taking tasks that logs its progress
  def runJob(user: String)
            (implicit logService: LogService
             , jobActor: ActorRef): Future[LogService] = {
    Future {
      logService.startLogging() // init the logging
      val results =
        for {
          i <- 2 to 3
          k <- 1 to 5
        } yield WizardDemoResult(s"Image Gallery $i - $k"
          , s"https://www.gstatic.com/webp/gallery$i/$k.png"
          , toISODateTimeString(LocalDateTime.now().minusHours(Random.nextInt(100))))

      results.foreach(doSomeWork)

      logService
    }
  }

  protected def doSomeWork(dr : WizardDemoResult)
                          (implicit logService: LogService): LogEntry = {
    Thread.sleep(500)
    val ll = Random.shuffle(List(DEBUG, DEBUG, INFO, INFO, INFO, WARN, WARN, ERROR)).head
    val detail = List(None, Some(s"Details for $jobLabel $ll: ${dr.name}")
                      , Some("Scala Adapters\nA simple framework to implement batch jobs - provides standard UI-clients to monitor and test them.\nLatest release 1.0.0"))(Random.nextInt(3))
    logService.log(ll, s"Job: $jobLabel $ll: ${dr.name}", detail)
  }
}



