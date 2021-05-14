import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {
    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("server start!");

        while (true) {
            Socket socket = serverSocket.accept();
            executor.submit(() -> handler(socket));
        }
    }

    public static void handler(Socket socket) {
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("线程信息：id=" + Thread.currentThread().getId() + "线程名称：" + Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

}
