package asuna.ezreal

import cats.implicits._
import monix.cats._
import asuna.common.BaseService
import asuna.proto.league.{ Region, Role }
import asuna.proto.league.lucinda.rpc.Constraints
import asuna.proto.league.lucinda.{ LucindaGrpc, StatisticsKey }
import asuna.proto.league.lucinda.rpc.GetStatisticsRequest
import asuna.proto.league.vulgate.Context
import asuna.proto.league.vulgate.rpc.GetAggregationFactorsRequest
import asuna.proto.league.vulgate.{ AggregationFactors, VulgateGrpc }
import monix.eval.Task
import monix.execution.Scheduler
import scala.concurrent.Future

class Ezreal(args: Seq[String])(implicit s: Scheduler) extends BaseService(args, EzrealConfigParser) {

  val lucinda = LucindaGrpc.stub(clientFor("lucinda"))
  val vulgate = VulgateGrpc.stub(clientFor("vulgate"))
  val cfg = config.service

  def fetchAggregationFactors(region: Region, patch: String): Task[AggregationFactors] = Task.deferFuture {
    vulgate.getAggregationFactors(
      GetAggregationFactorsRequest(
        context = Context(
          region = region,
          release = Context.Release.Patch(patch)
        ).some,
        patches = Seq(patch)
      )
    )
  }

  def fetchChampionStatistics(champion: Int, region: Region, patch: String, role: Role): Task[Unit] = {
    val req = GetStatisticsRequest(
      query = StatisticsKey(
        championIds = Seq(champion),
        enemyIds = Seq(),
        regions = Seq(region),
        roles = Seq(),
        tiers = Seq(),
        patches = Seq(patch),
        queues = Seq()
      ).some,

      constraints = Constraints(
        minPickRate = 0.05
      ).some
    )

    Task.deferFuture {
      println(s"START $champion $region $role $patch")
      lucinda.getStatistics(req.copy(query = req.query.map(_.copy(roles = Seq(role)))))
    }.map(_ => println(s"DONE $champion $region $role $patch"))
  }

  def run: Task[Unit] = {
    println("Time for a true display of skill!")
    val roles = (Role.values.toSet - Role.UNDEFINED_ROLE).toList
    (cfg.regions |@| cfg.patches |@| roles).map { (region, patch, role) =>
      for {
        factors <- fetchAggregationFactors(region, patch)

        _ <- factors.champions.toList.traverse { champ =>
          fetchChampionStatistics(champ, region, patch, role)
        }
      } yield ()
    }.sequence.map(_ => ())
  }

  def start: Future[Unit] = run.runAsync

}
