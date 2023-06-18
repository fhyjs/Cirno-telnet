package cn.fhyjs.cirno;

import java.util.Objects;

public abstract class Callback {
    StringBuilder s = new StringBuilder();
    public abstract void OnReceive(String msg);
    public abstract void OnExit();
    public void addrep(String responseString){
        if (Objects.equals(responseString, "\r")) return;
        if (Objects.equals(responseString, "\n")){
            this.OnReceive(s.toString());
            s=new StringBuilder();
            return;
        }
        s.append(responseString);
    }
}
