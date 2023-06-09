package com.github.pablowyourmind.sftptest.sftp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattedTimestampProvider {

    public static String getCurrentTimestampFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }
}
