package cn.fhyjs.cirno;

import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {
    public static void main(String[] arg) {
        Options options=new Options();
        options.addOption(Option
                .builder("h")
                .longOpt("host")
                .argName("host")
                .hasArg()
                .required(true)
                .desc("set host name")
                .build());
        options.addOption(Option
                .builder("?")
                .longOpt("help")
                .argName("help")
                .required(false)
                .desc("cmd help")
                .build());
        CommandLine cmd = null;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();
        try {
            cmd = parser.parse(options, arg);
            if (cmd.hasOption("?")) {
                throw new ParseException("help");
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helper.printHelp("Usage:", options);
            System.exit(-1);
        }
        try {
            Telnet telnet = new Telnet(cmd.getOptionValue("host"));
            Console c = new Console();
            telnet.setCallback(c);
            c.startinput(telnet);
            while (telnet.telnetClient.isConnected()){
                Thread.sleep(10);
            }
        } catch (IOException|ArrayIndexOutOfBoundsException e) {
            if (e instanceof ArrayIndexOutOfBoundsException)
                throw new RuntimeException("usage: --host <ip>:<port>");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}