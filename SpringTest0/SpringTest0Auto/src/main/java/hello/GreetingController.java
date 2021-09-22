package hello;




import datadog.trace.api.Trace;
import datadog.trace.api.interceptor.MutableSpan;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.aopalliance.intercept.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RestController
public class GreetingController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    HttpServletRequest request;

    // private static final java.util.logging.Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @Value("#{environment['sleeptime'] ?: '2000'}")
    private long sleepTime;


    @RequestMapping("/ServiceC")
    public String serviceC(HttpServletRequest request) throws Exception {
        //my map I'm creating for testing my own code:
        Map<String, String> map_test_z = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            System.out.print(key + "\n" + value + "\n");
            map_test_z.put(key, value);
        }
        Thread.sleep(50L);
        System.out.println("Right before multiply okokok");
        // logger.info("please log this");
        multiplyBy12((float) 23.3);

        doSomeOtherStuff(doSomeStuff("\n how about this but really"));


        //Post to downstream service using OKhttp specifically
        OkHttpClient client = new OkHttpClient();

        //hit the correct IP address both when running locally and when running in k8s
        String url = "";
        String k8s_springtest1_IP = System.getenv("SPRINGTEST1_SERVICE_HOST");
        System.out.println("this is the envar value " + k8s_springtest1_IP);
        if (k8s_springtest1_IP == null) {
            url = "http://localhost:9393/ServiceD";
        } else {
            url = "http://" + k8s_springtest1_IP + ":9393/ServiceD";
        }
        Request req = new Request.Builder()
                .url(url)
                .addHeader("this is a header", "here it is")
                .build();

        Response response = client.newCall(req).execute();

        String rs = response.body().string();


//Post to downstream service original
        //put headers into HttpEntity object for them to be sent over
        //HttpEntity<String> entity = new HttpEntity<>("body");
        // String rs = restTemplate.postForEntity("http://localhost:9393/ServiceD", entity, String.class).getBody();

        return rs;
    }


    @RequestMapping("/ServiceX")
    public String serviceX(HttpServletRequest request) throws InterruptedException, IOException {

        //hit the correct IP address both when running locally and when running in k8s
        //the k8s env "SPRINGTEST1_SERVICE_HOST" is automatically created and is the host IP of the SpringTes1 pod
        String url = "";
        String k8s_springtest1_IP = System.getenv("SPRINGTEST1_SERVICE_HOST");
        if (k8s_springtest1_IP == null) {
            url = "localhost:9393/ServiceY";
        } else {
            url = "http://" + k8s_springtest1_IP + ":9393/ServiceY";
        }


        HttpEntity<String> entity = new HttpEntity<>("body");
        String rs = restTemplate.postForEntity(url, entity, String.class).getBody();

        return rs;
    }



    @Trace(operationName = "sickest.job", resourceName = "MyJob.process")
    public String doSomeStuff (String somestring) throws InterruptedException {
        String helloStr = String.format("Hello, %s!", somestring);

        MutableSpan ddspan = (MutableSpan) GlobalTracer.get().activeSpan();
        ddspan.getLocalRootSpan().setTag("making_tags", "is_fun");

        Thread.sleep(200L);
        return helloStr;
    }


    public void doSomeOtherStuff (String somestring) throws InterruptedException {
        Tracer tracer = GlobalTracer.get();

        try (Scope scope = tracer.buildSpan("doSomeOtherStuff").startActive(true)) {
            scope.span().setTag("Service", "doSomeOtherStuffService");
            scope.span().setTag("here_a", "tag");


            Thread.sleep(1000);
        }
        System.out.println(somestring);
        Thread.sleep(200L);
    }

    @Trace(operationName = "multiply.twelve", resourceName = "ToTry.process")
    private float multiplyBy12 (Float num) throws InterruptedException {
        num = num * 12;
        System.out.print("here's the number:");
        System.out.print(num);
        Thread.sleep(250L);
        return num;
    }




}
