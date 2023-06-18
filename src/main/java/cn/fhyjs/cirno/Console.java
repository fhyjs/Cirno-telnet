package cn.fhyjs.cirno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Console extends Callback{
    getInput getinput;
    @Override
    public void OnReceive(String msg) {
        System.out.println(msg);
    }

    @Override
    public void OnExit() {

    }
    public void startinput(Telnet telnet){
        getinput=new getInput(telnet);
        getinput.start();
    }
    void stopinput(){
        getinput.stop();
        getinput=null;
    }
    private class getInput extends Thread{
        Telnet telnet;
        public getInput(Telnet telnet){
            super();
            this.telnet=telnet;
        }
        @Override
        public void run() {
            while (true) {
                try {
                    int t =  RawConsoleInput.read(false);
                    if (t<0) continue;
                    if (t==13) {
                        System.out.println();
                    }
                    else
                        System.out.print(Character.toChars(t));
                    telnet.send(String.valueOf((char) t));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
