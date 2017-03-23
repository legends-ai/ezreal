package asuna.ezreal

import asuna.common.config.ConfigParser
import asuna.proto.league.{ Region, Role, Tier }
import buildinfo.BuildInfo

class EzrealConfig {
  val regions = List(Region.NA)
  val patches = List("7.5")
}

object EzrealConfigParser extends ConfigParser[EzrealConfig](
  name = BuildInfo.name,
  version = BuildInfo.version,
  dependencies = Set("lucinda", "vulgate"),
  port = 29387,
  metaPort = 29388,
  initial = new EzrealConfig()
)
