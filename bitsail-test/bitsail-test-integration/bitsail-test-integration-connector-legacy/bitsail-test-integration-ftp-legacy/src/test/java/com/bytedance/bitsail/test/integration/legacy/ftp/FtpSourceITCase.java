/*
 * Copyright 2022-2023 Bytedance Ltd. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytedance.bitsail.test.integration.legacy.ftp;

import com.bytedance.bitsail.common.configuration.BitSailConfiguration;
import com.bytedance.bitsail.connector.legacy.ftp.common.FtpConfig;
import com.bytedance.bitsail.connector.legacy.ftp.option.FtpReaderOptions;
import com.bytedance.bitsail.test.integration.AbstractIntegrationTest;
import com.bytedance.bitsail.test.integration.legacy.ftp.container.FtpDataSource;
import com.bytedance.bitsail.test.integration.legacy.ftp.container.constant.FtpTestConstants;
import com.bytedance.bitsail.test.integration.utils.JobConfUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;

public class FtpSourceITCase extends AbstractIntegrationTest {
  protected FakeFtpServer ftpServer;

  @Before
  public void setup() {
    ftpServer = FtpDataSource.create();
    ftpServer.start();
  }

  @After
  public void teardown() {
    ftpServer.stop();
  }

  @Test
  public void testFtpSource() throws Exception {
    BitSailConfiguration globalConfiguration = JobConfUtils.fromClasspath("scripts/ftp_to_print.json");
    globalConfiguration.set(FtpReaderOptions.PROTOCOL, FtpConfig.Protocol.FTP.name());
    globalConfiguration.set(FtpReaderOptions.PORT, ftpServer.getServerControlPort());
    submitJob(globalConfiguration);
  }

  @Test
  public void testFtpSourceWithCharset() throws Exception {
    BitSailConfiguration globalConfiguration = JobConfUtils.fromClasspath("scripts/ftp_to_print.json");
    globalConfiguration.set(FtpReaderOptions.PROTOCOL, FtpConfig.Protocol.FTP.name());
    globalConfiguration.set(FtpReaderOptions.PORT, ftpServer.getServerControlPort());
    globalConfiguration.set(FtpReaderOptions.PATH_LIST, FtpTestConstants.UPLOAD_CHARSET);
    globalConfiguration.set(FtpReaderOptions.SUCCESS_FILE_PATH,
        FtpTestConstants.UPLOAD_CHARSET + FtpTestConstants.SUCCESS_TAG);
    globalConfiguration.set(FtpReaderOptions.CHARSET, FtpTestConstants.GBK_CHARSET);
    submitJob(globalConfiguration);
  }
}
