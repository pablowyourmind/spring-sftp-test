package com.github.pablowyourmind.sftptest.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SftpTest {

    private final String username = "";
    private final String password = "";
    private final String remoteHostname = "";
    private final int port = 22022;

    private final String knownHostsFile = "";

    @Test
    void putFileOnSftp() throws JSchException {
        ChannelSftp channelSftp = setupJsch();
        assertDoesNotThrow(() -> channelSftp.connect());
        String filename = "src/test/resources/samples/sample.txt";
        String remoteDirectory = username;

        assertDoesNotThrow(() -> channelSftp.put(filename, remoteDirectory + "/sample.txt"));
        channelSftp.exit();
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jSch = new JSch();
        jSch.setKnownHosts(knownHostsFile);
        Session jschSession = jSch.getSession(username, remoteHostname, port);
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }
}
