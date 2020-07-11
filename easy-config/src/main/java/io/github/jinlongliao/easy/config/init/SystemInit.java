package io.github.jinlongliao.easy.config.init;

import io.github.jinlongliao.easy.common.util.CliUtil;
import io.github.jinlongliao.easy.common.util.MixObject;
import io.github.jinlongliao.easy.config.server.ServerConfig;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:23
 */
public class SystemInit {
    public ServerConfig init(String... args) {
        ServerConfig serverConfig = new ServerConfig();
        final Options options = CliUtil.getDefaultOption();
        final CommandLine commandLine = CliUtil.parseCli(options, args);
        final Properties properties = new Properties();
        if (commandLine.hasOption(CliUtil.P)) {
            final String configPath = commandLine.getOptionValue(CliUtil.P);
            try {
                properties.load(Files.newInputStream(new File(configPath).toPath()));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        if (commandLine.hasOption(CliUtil.p)) {
            properties.put("server.port", commandLine.getOptionValue(CliUtil.p));
        }
        MixObject.mix(serverConfig, properties);
        return serverConfig;
    }
}
