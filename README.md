#What is hazelcast-cli?

hazelcast-cli is a management tool to install,configure,upgrage hazelcast on local,cloud from any OS.

#Installation

```brew install https://raw.githubusercontent.com/bilalyasar/homebrew/master/Library/Formula/hazelcast-cli.rb```

# Requirements

### hazelcast-cli.properties
You need to create a properties file in your home to define host machines to run hazelcast.

```
europe1.user=ubuntu
europe1.ip=ec2-54-159-97-238.compute-1.amazonaws.com
europe1.port=22
```

### SSH configuration
Your public key has to be configured on the target machine

# Operations

### Install Hazelcast

`hazelcast --install --hostname europe1 --version 3.4.1`

### Start an Hazelcast Node

`hazelcast --start --hostname europe1 --clustername wildcats --nodename tiger`

### Stop an Hazelcast Node

`hazelcast --stop --hostname europe1 --clustername wildcats --nodename tiger`

### Start Management Center
`hazelcast --startMC --hostname europe1`

### Rolling Upgrade Management Center
`hazelcast --upgrade --clustername wildcats --version 3.4.2`

# TODO

* more OS support. apt-get,exe,android
* starting hazelcast bundled tomcat










