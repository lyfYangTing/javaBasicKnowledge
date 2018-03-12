package util.fileUpload;

/**
 * 上传后的文件对象
 * @author liujiegang
 */
public class UploadedFile {

    private String originalFile;    //原始文件
    private String zoomFile;        //压缩文件

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public String getZoomFile() {
        return zoomFile;
    }

    public void setZoomFile(String zoomFile) {
        this.zoomFile = zoomFile;
    }
}
