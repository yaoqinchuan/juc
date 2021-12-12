package netty.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SingleReactorSingleThreadServer {
    private static final int PORT = 8001;

    private static ThreadPoolExecutor threadPool;

    static {
        threadPool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    public static void init() throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("一个客户端连接上来了");
            threadPool.submit(() -> {
                try {
                    handler(socketChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socketChannel.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
        }
    }

    private static void handler(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            while (true) {
                buffer.clear();
                int readCount = socketChannel.read(buffer);
                if (-1 == readCount) {
                    continue;
                }
                buffer.flip();
                System.out.println("接收到数据"+ new String(buffer.array()));
                socketChannel.write(buffer);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            System.out.println("关闭与" + socketChannel.getRemoteAddress() + "的连接");
            socketChannel.close();
        }
    }

    public static void main(String[] args) throws IOException {

        SingleReactorSingleThreadServer.init();
    }
}
