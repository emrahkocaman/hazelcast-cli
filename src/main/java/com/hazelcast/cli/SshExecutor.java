package com.hazelcast.cli;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by emrah on 12/06/15.
 */
public class SshExecutor {

    public static String exec(String user, String host, int port, String command, boolean breakProcess) throws Exception{

        Session session = null;
        ChannelExec channel = null;

        try {
            JSch jsch = new JSch();
            jsch.addIdentity("~/.ssh/id_rsa");
            session = jsch.getSession(user, host, port);
            Properties config = new Properties();

            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.setCommand(command);
            channel.connect();

            String msg = null;
            String out = null;
            while ((msg = in.readLine()) != null) {
                out = msg;
                if (breakProcess) {
                    break;
                }
            }
            return out;

        } catch (Exception e) {
            throw e;
        } finally {
            if (channel != null)
            channel.disconnect();
            if (session != null)
            session.disconnect();
        }
    }

}
