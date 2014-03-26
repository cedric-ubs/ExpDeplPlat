/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univubs.labsticc.expdeplplat;

import com.jcraft.jsch.*;
import java.io.*;

/**
 *
 * @author cedric
 */
public class CommandSender {

    String NODE = "jdoe@192.168.10.104";
    //String COMM = "xclock -display :0";
    String COMM1 = "./runDanahClient.sh lampeEIB.On";
    String COMM2 = "./runDanahClient.sh lampeEIB2.On";

    public CommandSender(String ONTO, String RES, String TASK) throws JSchException, IOException {
        /* String ONTO = args[0];
         * String NODE = args[1]
         * String RESOURCE = args[2];
         * String TASK = args[3];
         */

        String resource = RES;
        log(resource);
        
        JSch jsch = new JSch();
        
        String host = null;
        host = NODE;
        
        String user = host.substring(0, host.indexOf('@'));
        host = host.substring(host.indexOf('@') + 1);
        Session session = jsch.getSession(user, host, 22);

        if ("jdoe".equals(user)) {
            UserInfo ui = new MyUserInfo();
            session.setUserInfo(ui);
            session.connect();
            String command = null;
            /* Here we use static node and resources
             * however we should have the informations
             * directly from the ontology File.
             * A createCommand method will be 
             * implemented soon.
             */
            if ("_lampeEIB1".equals(resource) )
                command = COMM1;
            else if ("_lampeEIB2".equals(resource))
                command = COMM2;

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.setInputStream(null);

            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }
            }
            channel.disconnect();
            session.disconnect();
        } else {
            System.out.println("Unknown user " + user);
        }

        System.out.println("Command sent!");
    }

    public static class MyUserInfo implements UserInfo {

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            String passwd = null;
            passwd = "letmein";
            return passwd;
        }

        @Override
        public boolean promptPassword(String string) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String string) {
            return true;

        }

        @Override
        public boolean promptYesNo(String string) {
            return true;
        }

        @Override
        public void showMessage(String string) {
            log("Nothing to say!");
        }

    }

    private static void log(Object aObject) {
        System.out.println(String.valueOf(aObject));
    }
}
