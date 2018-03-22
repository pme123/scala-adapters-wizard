package server

import javax.inject.{Inject, Named, Singleton}

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import pme123.adapters.server.control.{JobActor, JobCreation}
import pme123.adapters.server.entity.AdaptersContext.settings.jobConfigs
import pme123.adapters.server.entity.ServiceException
import pme123.adapters.shared.JobConfig
import pme123.adapters.shared.JobConfig.JobIdent

import scala.concurrent.ExecutionContext

@Singleton
class WizardDemoJobCreation @Inject()(wizardDemoJob: WizardDemoProcess
                                   , @Named("actorSchedulers") val actorSchedulers: ActorRef
                                   , actorSystem: ActorSystem
                                  )(implicit val mat: Materializer
                                    , val ec: ExecutionContext)
  extends JobCreation {

  private val wizardDemoJobIdent: JobIdent = "wizardDemoJob"

  private lazy val wizardDemoJobRef =
    actorSystem.actorOf(JobActor.props(jobConfigs(wizardDemoJobIdent), wizardDemoJob), wizardDemoJobIdent)

  def createJobActor(jobConfig: JobConfig): ActorRef = jobConfig.jobIdent match {
    case "wizardDemoJob" => wizardDemoJobRef
    case other => throw ServiceException(s"There is no Job for $other")
  }

}
