package client;

import java.io.IOException;

public class ClientArgResolver {
    private static final int ARG_COUNT = 3;

    private static final int FILENAME_INDEX = 0;

    private static final int HOSTNAME_INDEX = 1;

    private static final int PORT_INDEX = 2;

    public static ClientContext resolve(String[] args) throws IOException {
        if(args.length != ARG_COUNT){
            throw new IOException("Wrong arguments count.\nUsage: java -jar clientName.jar <inputFile> <hostName> <port>");
        }

        return new ClientContext(args[FILENAME_INDEX], args[HOSTNAME_INDEX], Integer.parseInt(args[PORT_INDEX]));
    }
}
