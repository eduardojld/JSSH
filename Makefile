

all: build \
	run \
	

run:
	@java -cp jsch-0.1.41.jar:jt400.jar:log4j.jar:mysql-connector-java-5.1.8-bin.jar:substance.jar:apache-commons-net.jar:trident.jar:. com.epa.ssh.SshTestMultiple

help:
	@java -cp jsch-0.1.41.jar:jt400.jar:log4j.jar:mysql-connector-java-5.1.8-bin.jar:substance.jar:apache-commons-net.jar:trident.jar:. com.epa.ssh.SshTestMultiple -h

build:
	@javac -cp jsch-0.1.41.jar:jt400.jar:log4j.jar:mysql-connector-java-5.1.8-bin.jar:substance.jar:apache-commons-net.jar  com/epa/ssh/*.java com/jcraft/jzlib/*.java