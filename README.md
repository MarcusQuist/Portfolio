# Distributed_Systems
A project consisting of different distributed system architectures written in Java.

# PWC Case
Instructions on how to use the implementations are written in the PDF attached to the email conversation. 

If you are running the zookeeper and kafka implementation from the PWC_Case folder, be sure to modify the following properties to your own unique folder path:

zookeeper.properties:
#16: dataDir=.../kafka/data/zookeeper
E.g.: dataDir=C:/Users/Name/Desktop/kafka/data/zookeeper

server.properties:
#60: log.dirs=.../kafka/data/kafka
E.g. log.dirs=C:/Users/Marcu/Desktop/kafka/data/kafka

A topic for testing has been created with the following properties:
kafka-topics --zookeeper 127.0.0.1:2181 --topic new-client --create --partitions 3 --replication-factor 1

