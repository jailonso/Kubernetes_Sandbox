### Setup




command to run with auto tracing:  
java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.analytics.enabled=true -jar build/libs/springtest0-1.0.jar --server.port=9390
`
To build:
./gradlew build 
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`


Run with auto and add global span tags: `java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.global.tags=[food:grapes] -jar build/libs/springtest0-1.0.jar --server.port=9390`

To hit normal endpoint:

curl localhost:9390/ServiceC

Can also curl ServiceX to hit ServiceY on SpringTest1 `curl --header "name: zach" localhost:9390/ServiceX`



Side note: I also installed the latest tracer version and deleted the on that came with the project
Ω
When adding open tracing had to manually set the datadog ot tracing version

When using just the opentracer I ran the command: `java -Ddd.trace.analytics.enabled=true -jar build/libs/springtest0-1.0.jar --server.port=9390` should use port 9390 if connecting with separate application

When tracing using the Ddd.trace.methods system property run: `java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.methods=hello.GreetingController[doSomeStuff,doSomeOtherStuff]  -Ddd.jmxfetch.enabled=true  -jar build/libs/springtest0-1.0.jar --server.port=9393
` 


When you run service C and service D separately, for service c: java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -jar build/libs/springtest0-1.0.jar --server.port=9390
for service D: java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -jar build/libs/springtest0-1.0.jar --server.port=9393


To start just this service in debug mode writing logs to with_dd-trace-ot.log: 
java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.analytics.enabled=true -Ddd.jmxfetch.enabled=true -Ddd.trace.debug=true -Ddatadog.slf4j.simpleLogger.logFile=/Users/zach.groves/projects/my_sandboxes/java_apm_lab/auto/SpringTest0Auto/debug_logs.log -jar build/libs/springtest0-1.0.jar --server.port=9390

java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.analytics.enabled=true -Ddd.jmxfetch.enabled=true -Ddd.trace.debug=true -Ddatadog.slf4j.simpleLogger.logFile=/var/log/datadog/app_debug.log -jar build/libs/springtest0-1.0.jar --server.port=9390



/Users/zach.groves/projects/java-apm/dd-trace-java/dd-java-agent/build/libs/dd-java-agent-0.38.0-SNAPSHOT.jar

With profiling:


java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -XX:+FlightRecorder -Ddd.profiling.enabled=true -Ddd.profiling.api-key-file=./api_key -jar build/libs/springtest0-1.0.jar --server.port=9390

Trace agent logs sit in
code /var/log/datadog/trace-agent.log

Run with config file:

 java -javaagent:./dd-java-agent.jar -Ddd.trace.config=./datadog.properties  -jar build/libs/springtest0-1.0.jar --server.port=9390


java -javaagent:./dd-java-agent.jar -Ddd.service.name=springtest0 -Ddd.trace.analytics.enabled=true -Ddd.trace.debug=true  -Ddd.trace.report-hostname=true -Ddd.trace.global.tags=_dd.hostname:COMP10699.local -Ddatadog.slf4j.simpleLogger.logFile=/Users/zach.groves/projects/my_sandboxes/java_apm_lab/auto/SpringTest0Auto/app_logs.log  -jar build/libs/springtest0-1.0.jar --server.port=9390
