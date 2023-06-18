package cn.fhyjs.cirno;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetInputListener;
import org.apache.commons.net.telnet.TelnetNotificationHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Telnet {
    private Callback callback;
    public TelnetClient telnetClient;
    public Telnet(String hostname,int port) throws IOException {
        telnetClient = new TelnetClient();
        telnetClient.connect(hostname, port);
        telnetClient.registerInputListener(new IListener());
        new disconn().start();
    }
    public void send(String s) throws IOException {
        telnetClient.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));
        telnetClient.getOutputStream().flush();
    }
    public Telnet(String host) throws IOException {
        this(host.split(":")[0], host.split(":")[1]==null?22:Integer.parseInt(host.split(":")[1]));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void disconnect(){
        try {
            telnetClient.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        callback=null;
    }
    private class IListener implements TelnetInputListener {
        @Override
        public void telnetInputAvailable() {
            if (callback==null) return;
            InputStream is = telnetClient.getInputStream();
            String responseString;
            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder responseBuilder = new StringBuilder();
                while (is.available() > 0&&(bytesRead = is.read(buffer)) != -1) {
                    String response = new String(buffer, 0, bytesRead);
                    responseBuilder.append(response);
                }

                // 打印服务器回复
                responseString = responseBuilder.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            callback.addrep(responseString);
        }
    }
    private class disconn extends Thread{
        @Override
        public void run() {
            while (telnetClient.isAvailable()) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (callback!=null) callback.OnExit();
        }
    }
}
