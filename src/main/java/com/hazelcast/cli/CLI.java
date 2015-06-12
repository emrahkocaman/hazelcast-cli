package com.hazelcast.cli;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by emrah on 12/06/15.
 */
public class CLI {

    public static void main(String[] args) throws Exception {

        String userHome=System.getProperty("user.home");
        OptionParser optionParser = new OptionParser();
        OptionSpec install = optionParser.accepts("install", "Install hz");
        OptionSpec start = optionParser.accepts("start", "Start hz");
        OptionSpec startMC = optionParser.accepts("startMC", "Start hz mancenter");
        OptionSpec hostName = optionParser.accepts("hostname").withRequiredArg().ofType(String.class).required();
        OptionSpec version = optionParser.accepts("version").withRequiredArg().ofType(String.class);
        OptionSpec optionClusterName = optionParser.accepts("clustername").withRequiredArg().ofType(String.class);
        OptionSpec optionNodeName = optionParser.accepts("nodename").withRequiredArg().ofType(String.class);
        OptionSpec optionConfigFile = optionParser.accepts("configfile").withRequiredArg().ofType(String.class);

        OptionSet result = optionParser.parse(args);

        String host = (String) result.valueOf(hostName);
        Properties properties = getProperties(userHome);
        String user = properties.getProperty(host + ".user");
        String hostIp = properties.getProperty(host + ".ip");
        int port = Integer.parseInt(properties.getProperty(host + ".port"));

        CommandBuilder commandBuilder = new CommandBuilder();

        if (result.has(install)) {
            if (!result.has(version)) {
                System.out.println("--version required");
                System.exit(-1);
            }
            String strVersion = (String) result.valueOf(version);
            String command = commandBuilder.wget(strVersion);
            System.out.println("Download started...");
            String output = SshExecutor.exec(user, hostIp,
                    port, command, false);
            System.out.println("Extracting...");
            String extractCommand = commandBuilder.extract();
            SshExecutor.exec(user, hostIp, port, extractCommand, false);

            String move = commandBuilder.move("hazelcast-" + strVersion, "hazelcast");
            SshExecutor.exec(user, hostIp, port, move, false);
            System.out.println("Installation completed...");

        } else if (result.has(start)) {
            String clusterName = (String) result.valueOf(optionClusterName);
            String nodeName = (String) result.valueOf(optionNodeName);
            String configFile = (String) result.valueOf(optionConfigFile);
            String cmd = commandBuilder.upload(user, hostIp, nodeName, configFile);
            System.out.println("Uploading config file...");
            Runtime.getRuntime().exec(cmd);
            System.out.println("Upload completed.");
            System.out.println("Starting instance...");
            String startCmd = commandBuilder.start("hazelcast-" + nodeName + ".xml");
            String pid = SshExecutor.exec(user, hostIp, port, startCmd, true);
            System.out.println("Instance started : " + pid);

        } else if(result.has(startMC)) {
            SshExecutor.exec(user, hostIp, port, commandBuilder.startMC(), false);
        }
    }

    private static Properties getProperties(String userHome) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "cli.properties";
//            input = CLI.class.getClassLoader().getResourceAsStream(filename);
            //load a properties file from class path, inside static method

            prop.load(new InputStreamReader(new FileInputStream(userHome+"/cli.properties")));
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
