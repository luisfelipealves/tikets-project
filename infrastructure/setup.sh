#!/bin/bash
echo "Starting infrastructure..."

kind create cluster --name ticket-portfolio --config=kind-config.yaml

docker-compose up -d kafka zookeeper

kubectl apply -f postgres.yaml
kubectl apply -f gateway.yaml

echo "All ready! Infrastructure is up and running."