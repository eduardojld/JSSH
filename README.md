# JSSH

This project was the result of just getting tired of executing manual support tasks, it was build back in 2009 and it basically was meant to ease those manual task that included

 - Copy files throu SSH from a source to multiple destinations 

 After we knew this little utility was working we starting to have ideas and other requirements to make those support task easier

 - Retrieve files from multiple sources to one destination
 - Execute bash commads on multiple destinations
 - Execute queries on multiple destionations 

 This changed the way we did support for Retail Stores enabling the support team to perform their task faster

 The utility can be run with an UI or on console mode

 # Configuration Files

 ## ips.properties
 Set of ips to connect to

 ## comandos.properties
 Commands to be executed

 ## sql.properties
Queries to be performed (mySQL and DB2/400 drivers on the repo)

# How to run

You need to have Java installed and run

```
$ make
```

For help on running it on consolde mode run

```
$ make help
```

# TODOs

Again this was build quite a while ago, so there are a couple of things that need to be revisited:
 - Folder structure
 - Dependency Injection
 - Tests
 - The code could use some refactoring for sure
 - Managed Dependencies
 - Container availability 