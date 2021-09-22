
# Intro

This repo showcases two Kubernetes-based Java Services that interact and getting distributed traces from them. The applications use both manual and automatic instrumentation. You can use this to change config options for Java services and quickly redeploy them to see their behavior. You can also change the code itself. 


## Set up

### If you don't have Minikube 
Install Minikube & Start:
```
curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-darwin-amd64 \
&& chmod +x minikube \
&& mv ./minikube /usr/local/bin \
&& minikube start --extra-config=kubelet.read-only-port=10255
```

Install kubectl:
```
curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/darwin/amd64/kubectl \
&& chmod +x ./kubectl \
&& mv ./kubectl /usr/local/bin/kubectl
```



### You have Minikube
Run `minikube start` to start up a cluster

Insert your API key into this command and run:

`kubectl create secret generic datadog-secret --from-literal api-key="${DD_API_KEY}" --namespace="default"`

Run `./setup.sh`


## Build & Deploy

Run the script which will build the springtest0 and springtest1 application containers, deploy all of the manifests, and then curl two endpoints of the springtest0 service.

`./build.sh`

You should now be able to see these traces in the UI. You should also be able to see debug application logs coming from both Java applications in the Datadog UI.

## Get Some Traces

The curl commands to run to hit the two endpoints on Springtest0 should be printed by the `repeat_pinging.sh` script which'll ping the endpoints repeatedly every 5 seconds. The script can be run with'

`
./repeat_pinging.sh
`

## Edit the code
If you'd like to make changes to the code, the code for SpringTest0 exists at `SpringTest0/SpringTest0Auto/src/main/java/hello`

SpringTest1 exists at `SpringTest1/SpringTest1Auto/src/main/java/hello`

The tracer is initialized in `Application.java` but the main code for manual instrumentation (along with what is instrumented automatically) exists in `GreetingController.java`

In order to edit the code, I'd recommend downloading IntelliJ IDE. From there you can choose "New project from existing sources" and select "gradle" when it asks what kind of project it is: https://www.jetbrains.com/idea/

However, you don't have to and the code can be edited in any text editor.

After making your code changes, run the build.sh script to rebuild the containers, redeploy the pods, and curl SpringTest0.
`. build.sh`


## The below steps essentially carry out what the build script does for you. Normally I'd recommend just running the script because it's fast and easy, but if you'd like to better understand how the script works, feel free to try the below instead of running the script: 

## Deploy Things

Deploy the Datadog Agent
```
kubectl create -f datadog.yaml
```

Deploy SpringTest1 App
```
kubectl create -f springtest1_deployment.yaml
```

Deploy SpringTest0 App
```
kubectl create -f springtest0_deployment.yaml
```



## Get Some Traces the hard way

The curl commands to run to hit the two endpoints on Springtest0 should be printed by the `repeat_pinging.sh` script which'll ping the endpoints repeatedly every 5 seconds. The script can be run with'

`
./repeat_pinging.sh
`

If you'd like to figure it out manually, for learning purposes, do the following:

Run `kubectl get pods` to make sure that you have the agent and both apps running. For example:
```
COMP10699:kubernetes_datadog zach.groves$ kubectl get pods
NAME                           READY   STATUS    RESTARTS   AGE
datadog-agent-z4qhw            1/1     Running   0          52m
springtest0-84dfdd9ffb-lps87   1/1     Running   0          51m
springtest1-858f69c44d-7tfzv   1/1     Running   0          51m
```

Run `kubectl get services` to get the IP address of sprintest0 pod:
```
COMP10699:kubernetes_datadog zach.groves$ kubectl get services
NAME          TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
kubernetes    ClusterIP      10.96.0.1        <none>        443/TCP          21d
springtest0   LoadBalancer   10.107.175.125   <pending>     9390:30751/TCP   20d
springtest1   LoadBalancer   10.101.157.204   <pending>     9393:30707/TCP   21d
```
In this case the value is `10.107.175.125`

Exec into the Datadog agent pod and curl to hit SpringTest0:

```
kubectl exec -it <datadog agent pod name> -- curl <sprintest0 IP>:9390/ServiceC
```
You should see the response `Service D` which is returned from the SpringTest1 app after SpringTest0 makes a call to it.

You can also hit endpoint ServiceX on SpringTest0 for a different trace:
```
kubectl exec -it <datadog agent pod name> -- curl <sprintest0 IP>:9390/ServiceX
```
You should see the response `Service Y` which is returned from the SpringTest1 app

You should now be able to see these traces in the UI. You should also be able to see debug application logs coming from both Java applications in the Datadog UI.



