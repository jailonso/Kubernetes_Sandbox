#!/bin/bash

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

#!/bin/bash
for i in {1..10000}
  do
    echo "hitting endpoints of Springtest0"
    echo "kubectl exec -it $agent_pod -- curl $ip:9390/ServiceC"

    kubectl exec -it $agent_pod -- curl $ip:9390/ServiceC

    echo "kubectl exec -it $agent_pod -- curl $ip:9390/ServiceX"

    kubectl exec -it $agent_pod -- curl $ip:9390/ServiceX
    sleep 5
  done

