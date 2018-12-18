package czbk.io.pipedStream;

import java.io.IOException;
import java.io.PipedOutputStream;

/**
 * Created by 18435 on 2018/11/27.
 */
public class WriterRunable implements Runnable{
    private PipedOutputStream pos;

    public WriterRunable(PipedOutputStream pos) {
        this.pos = pos;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            pos.write("piped writer".getBytes());
            System.out.println("已写入");
            pos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
