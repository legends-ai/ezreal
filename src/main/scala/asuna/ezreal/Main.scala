package asuna.ezreal

import monix.execution.Scheduler
import scala.concurrent.Await
import scala.concurrent.duration.Duration


object Main {

  def main(args: Array[String]): Unit = {
    implicit val sched = Scheduler.io()
    Await.result(new Ezreal(args).start, Duration.Inf)
  }

}
