package com.hazelcast.cli;

/**
 * Created by emrah on 12/06/15.
 */
public class CommandBuilder {

    private static final String HZ_DOWNLOAD_URL = "\"http://download.hazelcast.com/download.jsp?version=hazelcast-#version#&type=tar&p=hazelcast-cli\"";

    public String wget(String version) {
        String url = HZ_DOWNLOAD_URL.replace("#version#", version);
        return "wget -O hazelcast" + ".tar.gz " + url ;
    }

    public String extract() {
        return "tar -zxvf hazelcast" + ".tar.gz";
    }

    public String move(String original, String target ) {
        return "mv " + original + " " + target;
    }

    public String start(String configFile) {
        return "java -cp \"hazelcast/lib/*\" -Dhazelcast.config=" +
                configFile + " com.hazelcast.core.server.StartServer & echo $!";
    }

    public String startMC() {
        return "java -jar hazelcast/mancenter/mancenter.war";
    }
    public String upload(String user, String hostName, String nodeName, String configFile) {
        return "scp " + configFile + " " + user + "@" + hostName + ":~/" + "hazelcast-" + nodeName + ".xml";
    }

}
