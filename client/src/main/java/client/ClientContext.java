package client;

public class ClientContext {
    private String fileName;
    private String hostName;
    private int port;

    public ClientContext(String fileName, String hostName, int port) {
        this.fileName = fileName;
        this.hostName = hostName;
        this.port = port;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }
}