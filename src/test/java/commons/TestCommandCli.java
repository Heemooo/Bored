package commons;

import org.apache.commons.cli.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCommandCli {
    public static void main(String[] args) {
        String[] arg = {"new","theme","hello"};
        try {
            testOption(arg);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    static void testOption(String[] args) throws ParseException{
        Options options = new Options();
        options.addOption("n","new", false, "display current time");
        options.addOption("c", "theme",true, "country code");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);
        System.out.println(cmd.toString());
        if(cmd.hasOption("new")) {
            System.out.println(cmd.hasOption("theme"));
            System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date())+" in "+cmd.getOptionValue("c"));
        }else {
            System.out.println((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
        }
    }
}
