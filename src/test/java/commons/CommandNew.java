package commons;

import lombok.ToString;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.concurrent.Callable;

@ToString
@Command(name = "new")
public class CommandNew implements Callable {

    @ArgGroup(multiplicity = "1")
    Exclusive exclusive;

    @Override
    public Object call() throws Exception {
        return null;
    }

    @ToString
    static class Exclusive {
        @Option(names = {"site", "-s"}, description = "SiteName", order = 1, required = true)
        private String site;

        @Option(names = {"theme", "-t"}, description = "ThemeName", order = 2, required = true)
        private String theme;

        @Option(names = {"page", "-p"}, description = "PageName", order = 3, required = true)
        private String page;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CommandNew()).execute(args);
        System.exit(exitCode);
    }
}
