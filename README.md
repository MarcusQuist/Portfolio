# Distributed Systems
A project consisting of different distributed system architectures written in Java.
\
\
**Summary**
* ClientServer _(Echo Server)_
* ClientServerManaged _(Multiple servers / channels)_
* ClientServerMulti _(Simple Client-Server Architecture without channels)_
* P2P _(Peer-to-Peer)_
* PublishSubscribe _(Publish-Subscribe Architecture without a broker)_
* PublishSubscribeBroker _(Publish-Subscribe Architecture with a broker)_
* UDP _(UDP instead of TCP being used)_
* UDP_Miniproject _(UDP modified for a school assignment)_
* UDP_Reliable _(UDP made "reliable" as part of a school assignment)_

# Distributed Auction house
A project consisting of a client-server architecture written as part of a final exam within the Mobile and Distributed Systems course
\
Documentation and usage guide has been placed within the folder under the file-name "Documentation".
# PWC Case
**Producer** \
It has been assumed that a Kafka Producer may send more than one record.\
The user is asked to enter a clientId and clientName for each record to be sent.

**Consumer** \
It issumed that new consumers must not be met with all prior data sent to the topic –  only the data sent after the consumer has joined. \
In order to run the Kafka Consumer program, a groupId must be given as the first argument.


If you are running the zookeeper and kafka implementation from the PWC_Case folder, be sure to modify the following properties to your own unique folder path:

**zookeeper.properties:**  
#16: dataDir=.../kafka/data/zookeeper\
E.g.: dataDir=C:/Users/Name/Desktop/kafka/data/zookeeper

**server.properties:**  
#60: log.dirs=.../kafka/data/kafka\
E.g. log.dirs=C:/Users/Name/Desktop/kafka/data/kafka

A topic for testing has been created with the following properties:\
kafka-topics --zookeeper 127.0.0.1:2181 --topic new-client --create --partitions 3 --replication-factor 1

