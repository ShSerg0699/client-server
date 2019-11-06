package server;

public class Main {
    public static void main(String[] args) {
        try {
            new Server(ServerArgResolver.resolve(args)).run();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}