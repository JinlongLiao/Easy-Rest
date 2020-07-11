package io.github.jinlongliao.easy.common.util;

import org.apache.commons.cli.*;

/**
 * @author liaojinlong
 * @since 2020/7/10 10:19
 */
public class CliUtil {
    public static String P = "P";
    public static String p = "p";
    public static String h = "h";
    private static CommandLineParser commandLineParser = new DefaultParser();

    public static Options getDefaultOption() {
        Options options = new Options();
        // host
        options.addOption(Option.builder(h).argName("ipv4 or ipv6").required(false).hasArg(true).longOpt("host").type(String.class).desc("the host of remote server").build());
        // port
        options.addOption(Option.builder(p).hasArg(true).longOpt("port").required(false).type(Short.TYPE).desc("the port of remote server").build());
        // config path
        options.addOption(Option.builder(P).hasArg(true).longOpt("path").required(false).type(Short.TYPE).desc("the config path of  server").build());
        return options;
    }

    public static CommandLine parseCli(Options options, String... args) {
        try {
            return commandLineParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }
}