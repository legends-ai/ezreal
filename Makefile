lucindatunnel:
	lsof -twni tcp:31310 | xargs kill -9
	ssh -fNL 31310:lucinda.marathon.mesos:31310 -p 2200 kirito@asunad-master.southcentralus.cloudapp.azure.com

vulgatetunnel:
	lsof -twni tcp:6205 | xargs kill -9
	ssh -fNL 6205:vulgate.marathon.mesos:6205 -p 2200 kirito@asunad-master.southcentralus.cloudapp.azure.com

tunnel: lucindatunnel vulgatetunnel

run:
	sbt "runMain asuna.ezreal.Main --lucinda_port=31310 --vulgate_port=6205"
