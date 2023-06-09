package com.github.pablowyourmind.sftptest.sftp;

import com.github.pablowyourmind.sftptest.sftp.utils.FormattedTimestampProvider;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@TestPropertySource("classpath:/properties/sftp-test.properties")
class SftpTest {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Value("${sftp.username}")
    private String username;

    @Value("${sftp.password}")
    private String password;

    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.knownHostsFile}")
    private String knownHostsFile;

    @Value("${sftp.knownHostsFile}")
    private String sampleTestFile;

    private String remoteFilename;

    @PostConstruct
    public void setupRemoteFilename() {
        remoteFilename = getRemoteFilenameWithFormattedTimestamp();
        logger.info("Remote filename: {}", remoteFilename);
    }

    @Test
    void addAndRemoveFileOnSftp() throws JSchException {
        ChannelSftp channelSftp = setupJsch();

        logger.info("Connecting to the SFTP server: {}@{}", username, host);
        assertDoesNotThrow(() -> channelSftp.connect());
        logger.info("Connected to the remote SFTP server");

        assertDoesNotThrow(() -> channelSftp.put(sampleTestFile, remoteFilename));
        logger.info("File {} uploaded on the server", remoteFilename);

        assertDoesNotThrow(() -> channelSftp.rm(remoteFilename));
        logger.info("File {} deleted on the remote server", remoteFilename);

        channelSftp.exit();
        logger.info("Connection closed");
    }

    private ChannelSftp setupJsch() throws JSchException {
        JSch jSch = new JSch();
        jSch.setKnownHosts(knownHostsFile);
        Session jschSession = jSch.getSession(username, host, port);
        jschSession.setPassword(password);
        jschSession.connect();
        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    private String getRemoteFilenameWithFormattedTimestamp() {
        return String.format("%s/sample%s.txt", username, FormattedTimestampProvider.getCurrentTimestampFormatted());
    }
}
