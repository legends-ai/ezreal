package asuna.ezreal

import monix.execution.Scheduler


object Main {

  def main(args: Seq[String]): Unit = {
    implicit val sched = Scheduler.io()
    new Ezreal(args).start
  }

}
