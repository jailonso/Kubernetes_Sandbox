#!/usr/bin/env bash

#check if user has minkube based on the output of the minikube command
#if you have minikube installed but aren't using it, comment out the below if statement.
minkube_output=$(minikube)
SUB='command not found'
if [ "$minikube_output" != *"$SUB"* ]
then
  echo "Minikube detected, will create docker images for minikube"
  eval $(minikube -p minikube docker-env) #change test1 to minikube
fi
#Build containers which in their Dockerfiles also build the application jars
docker build -t zstriker19/springtest0:2 ./SpringTest0/ 


docker build -t zstriker19/springtest1:2 ./SpringTest1/

#Deploy daemonsets:
kubectl delete svc,deploy,daemonset --all
kubectl apply -f .

echo "Sleeping for 30s before pulling pod name and service IP"
sleep 40


#grab the SpringTest0 service IP address:
messy_ip=$(kubectl get service | awk -F ' ' '{print $3}')
ip="$(echo $messy_ip | cut -d' ' -f3)"


echo "here's the ip we grabbed for the SpringTest0 service:"
echo $ip

#grab the agent pod name:
echo "here's the Datadog agent pod name:"
messy_agent_pod=$(kubectl get pods | awk -F ' ' '{print $1}')
agent_pod="$(echo $messy_agent_pod | cut -d' ' -f2)"
echo $agent_pod


#curl the service's two endpoints:
echo "hitting endpoints of Springtest0"
echo "kubectl exec -it $agent_pod -- curl $ip:9390/ServiceC"

kubectl exec -it $agent_pod -- curl $ip:9390/ServiceC

echo "kubectl exec -it $agent_pod -- curl $ip:9390/ServiceX"

kubectl exec -it $agent_pod -- curl $ip:9390/ServiceX