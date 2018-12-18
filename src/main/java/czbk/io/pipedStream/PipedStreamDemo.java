package czbk.io.pipedStream;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by 18435 on 2018/11/27.
 *
 * Properties 集合中涉及流的
 * PipedInputStream   PipedOutputStream  涉及到多线程的IO流对象
 * 输入输出可以直接进行连接，通过结合线程使用
 *
 * 1 个线程读  1个线程写  在同一个线程可能会出现死锁
 *
 * 管道输入流应该连接到管道输出流；
 * 然后管道输入流提供写入管道输出流的任何数据字节。
 * 通常，数据由一个线程从PipedInputStream对象读取，而数据则由其他线程写入相应的PipedOutputStream。
 * 不建议尝试使用来自单个线程的两个对象，因为它可能会导致线程死锁。
 * 管道输入流在限制范围内包含一个缓冲区，将读操作与写操作分离开来。
 * 如果向连接的管道输出流提供数据字节的线程不再活动，则管道就会中断。
 */
public class PipedStreamDemo {

    public static void main(String[] args) throws IOException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();
        pos.connect(pis);

        ReaderRunable rr = new ReaderRunable(pis);
        WriterRunable wr = new WriterRunable(pos);

        new Thread(rr).start();
        new Thread(wr).start();

    }
}
