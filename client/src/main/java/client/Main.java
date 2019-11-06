package client;

public class Main {
    public static void main(String[] args) {
        try {
            new Client(ClientArgResolver.resolve(args)).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}