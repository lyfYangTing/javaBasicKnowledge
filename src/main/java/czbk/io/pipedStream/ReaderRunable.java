package czbk.io.pipedStream;

import java.io.IOException;
import java.io.PipedInputStream;

/**
 * Created by 18435 on 2018/11/27.
 */
public class ReaderRunable implements Runnable{

    private PipedInputStream pis;

    public ReaderRunable(PipedInputStream pis) {
        this.pis = pis;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        try {
            System.out.println("等待写入");
            int len = pis.read(data);
            System.out.println("读出的数据：" + new String(data,0,len));
            pis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
