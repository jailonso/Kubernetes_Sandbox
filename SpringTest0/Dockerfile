FROM java:8

COPY . /home
WORKDIR /home/SpringTest0Auto

RUN curl -o dd-java-agent.jar 'https://repository.sonatype.org/service/local/repositories/central-proxy/content/com/datadoghq/dd-java-agent/0.68.0/dd-java-agent-0.68.0.jar'


RUN ./gradlew build



CMD java -javaagent:dd-java-agent.jar \
    -jar /home/SpringTest0Auto/build/libs/springtest0-1.0.jar \
    --server.port=9390
