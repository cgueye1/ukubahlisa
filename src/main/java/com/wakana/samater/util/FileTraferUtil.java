package com.wakana.samater.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FileTraferUtil {
    
     public static void transferFileToRemote(String localFilePath, String remoteFilePath) {
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession("root", "89.38.135.235", 22);
            session.setPassword("Wakan@123");
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (InputStream inputStream = new FileInputStream(localFilePath)) {
                channelSftp.put(inputStream, remoteFilePath);
            }
        } catch (JSchException | SftpException | IOException e) {
            e.printStackTrace(); // Gérer les exceptions correctement dans votre application
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
