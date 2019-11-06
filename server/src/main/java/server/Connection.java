package server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Connection implements Runnable {
    private Socket socket;
    private static final int BUFF_SIZE = 104857;
    private static final String FOLDER_NAME = "uploads/";
    private static final long MAX_FILE_SIZE = 1099511627776L;
    private static final int MAX_FILENAME_SIZE = 4096;
    private static final int FILENAME_BYTES = 4;
    private static final int FILESIZE_BYTES = 8;
    private static final byte ERROR_FLAG = 1;
    private static final byte OK_FLAG = 0;
    private long sessionTime = 0;
    private long averageTime = 0;
    private long instantTime = 0;
    private long averageCount = 0;
    private long instantCount = 0;



    public Connection(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run(){
        System.out.println("New connection: " + socket.getInetAddress());
        int byteCount;
        long byteSum = 0;
        byte[] buf = new byte[BUFF_SIZE];

        try{
            OutputStream fileOutputStream = new FileOutputStream(getFileName());
            long fileSize = getFileSize();


            boolean showed = false;
            sessionTime = System.currentTimeMillis();
            averageTime = System.currentTimeMillis();
            instantTime = System.currentTimeMillis();
            for (; byteSum < fileSize; byteSum += byteCount) {
                byteCount = socket.getInputStream().read(buf);
                if((System.currentTimeMillis()-averageTime) < 3000) {
                    averageCount+=byteCount;
                    instantCount+=byteCount;
                }
                else
                {
                    printSpeed();
                    showed = true;
                }
                fileOutputStream.write(buf, 0, byteCount);
            }
            if(!showed) {
                printSpeed();
            }
            fileOutputStream.close();

        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }finally {
            try {
            socket.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        System.out.println("Connection finish: " + socket.getInetAddress());
    }


    private File getFileName() throws IOException {
        int fileNameSize = ByteBuffer.wrap(socket.getInputStream().readNBytes(FILENAME_BYTES)).getInt();
        if (fileNameSize > MAX_FILENAME_SIZE || fileNameSize == 0) {
            sendFlag(socket.getOutputStream(), ERROR_FLAG);
            throw new IOException("Wrong file name size");
        }
        sendFlag(socket.getOutputStream(), OK_FLAG);

        byte[] fileNameBytes = new byte[fileNameSize];
        socket.getInputStream().readNBytes(fileNameBytes, 0, fileNameSize);
        String fileName = new String(fileNameBytes, "UTF-8");
        System.out.println(fileName);
        return new File(FOLDER_NAME + correctFileName(fileName));
    }

    private String correctFileName(String fileName){
        int pos1 =  fileName.lastIndexOf("../");
        int pos2 = fileName.lastIndexOf("..\\");
        if(pos1 > 0){
            fileName = fileName.substring(pos1 + 3);
        }
        if(pos2 > 0){
          fileName = fileName.substring(pos2 + 3);
        }
        return fileName;
    }

    private void sendFlag(OutputStream outputStream, byte flag) throws IOException {
        outputStream.write(flag);
        outputStream.flush();
    }

    private long getFileSize() throws IOException {
        long fileSize = ByteBuffer.wrap(socket.getInputStream().readNBytes(FILESIZE_BYTES)).getLong();
        if(!(fileSize > 0 && fileSize < MAX_FILE_SIZE)){
            sendFlag(socket.getOutputStream(),ERROR_FLAG);
            throw new IOException("Wrong file size");
        }
        sendFlag(socket.getOutputStream(),OK_FLAG);
        return fileSize;
    }

    private void printSpeed()
    {
        long now = System.currentTimeMillis();
        System.out.println("Instant speed: " + instantCount/(now - instantTime) * 1000 + " byte/sec");
        double speed = (double)averageCount * 1000 / (double)(now - sessionTime);
        System.out.println("Average speed: " + ((int)(speed)) + " byte/sec");
        averageTime = now;
        instantTime = now;
        instantCount = 0;
    }


}


