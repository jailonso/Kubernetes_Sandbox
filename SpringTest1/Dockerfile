FROM java:8


COPY . /home
WORKDIR /home/SpringTest1Auto


RUN curl -o dd-java-agent.jar 'https://repository.sonatype.org/service/local/repositories/central-proxy/content/com/datadoghq/dd-java-agent/0.68.0/dd-java-agent-0.68.0.jar'


RUN ./gradlew build

CMD java -javaagent:dd-java-agent.jar \
    -jar /home/SpringTest1Auto/build/libs/springtest1-1.0.jar \
    --server.port=9393