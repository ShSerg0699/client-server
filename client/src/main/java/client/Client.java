package client;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Client {
    private String hostName;
    private int port;
    private String fileName;
    private static final int BUFF_SIZE = 1048576;
    private static final int ERROR = 1;
    private static final int FILENAME_BYTES = 4;

    public Client(ClientContext clientContext) {
        this.hostName = clientContext.getHostName();
        this.port = clientContext.getPort();
        this.fileName = clientContext.getFileName();
    }

    public void run(){
        int count;
        byte[] buf = new byte[BUFF_SIZE];
        File inputFile = new File(fileName);
        Socket socket;

        try{
            socket = new Socket(hostName, port);
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(inputFile));

            byte[] fileNameBytes = inputFile.getName().getBytes(Charset.forName("UTF-8"));
            socket.getOutputStream().write(ByteBuffer.allocate(FILENAME_BYTES).putInt(fileNameBytes.length).array());
            checkAnswer(socket.getInputStream(), "Wrong file name size");
            socket.getOutputStream().write(fileNameBytes);//add check filename size

            long fileSize = inputFile.length();
            socket.getOutputStream().write(ByteBuffer.allocate(Long.BYTES).putLong(fileSize).array()); //add check

            while((count = fileInputStream.read(buf)) > 0 ){
                socket.getOutputStream().write(buf, 0, count);
            }
            checkAnswer(socket.getInputStream(), "Error occurred during transmission");
            System.out.println("\nFile: " + inputFile.getName() + " has been sent");
            socket.close();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("\n" + e.getLocalizedMessage());
            System.out.println("\nFile: " + inputFile.getName() + " has not been sent");
        }

    }

    private void checkAnswer(InputStream inputStream, String errorMessage) throws IOException {
        int flag = inputStream.read();
        if(flag == ERROR){
            throw new IOException(errorMessage);
        }
    }
}
