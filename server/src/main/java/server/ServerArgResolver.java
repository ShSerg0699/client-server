package server;

import java.io.IOException;

public class ServerArgResolver {
    private static final int ARG_COUNT = 1;

    private static final int PORT_INDEX = 0;

    public static int resolve(String[] args) throws IOException {
        if (args.length != ARG_COUNT) {
            throw new IOException("Wrong arguments count.\nUsage: java -jar serverName.jar <port>");
        }

        return Integer.parseInt(args[PORT_INDEX]);
    }
}