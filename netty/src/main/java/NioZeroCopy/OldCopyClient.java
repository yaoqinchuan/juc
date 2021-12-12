package NioZeroCopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class OldCopyClient {
    public void init() throws IOException {
        Socket socket = new Socket("localhost", 7070);

        String fileName = "G:\\code\\netty\\src\\main\\java\\NioZeroCopy\\file1.pdf";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4097];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = fileInputStream.read(buffer)) > 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总的字节数: " + total + ", 耗时: " + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }

    public static void main(String[] args) throws IOException {
        OldCopyClient oldCopyClient = new OldCopyClient();
        oldCopyClient.init();
    }
}
