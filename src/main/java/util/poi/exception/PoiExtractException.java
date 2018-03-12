package util.poi.exception;

/**
 * poi 抽取文件内容异常
 *
 * @author luffy
 * @date 15/11/18
 */
public class PoiExtractException extends RuntimeException {

    public PoiExtractException() {super();}

    public PoiExtractException(String message) {super(message);}

    public PoiExtractException(String message, Throwable e) {super(message, e);}

    public PoiExtractException(Throwable e) {super(e);}
}
