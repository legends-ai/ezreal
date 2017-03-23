package asuna.ezreal

import asuna.common.config.ConfigParser
import asuna.proto.league.{ Region, Role, Tier }
import buildinfo.BuildInfo

class EzrealConfig {
  val regions = List(Region.NA, Region.EUW, Region.BR)
  val patches = List("7.5", "7.4", "7.3", "7.2", "7.1", "6.24", "6.23", "6.22")
}

object EzrealConfigParser extends ConfigParser[EzrealConfig](
  name = BuildInfo.name,
  version = BuildInfo.version,
  dependencies = Set("lucinda", "vulgate"),
  port = 29387,
  metaPort = 29388,
  initial = new EzrealConfig()
)
