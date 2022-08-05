import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
public static final String hostName="127.0.0.1";
public static final int port=30555;
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(hostName, port));
        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    String msg = new String(inputBuffer.array(), 0, bytesCount,
                            StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Message from client:" + msg);
                    msg = msg.replace(" ", "");
                    socketChannel.write(ByteBuffer.wrap(("Server: (message without spaces) " +
                            msg).getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }
}
