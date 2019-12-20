package Client.Control;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.stream.*;

public class ChupManHinh {
  // Thread lưu trữ số byte trước đó đã chụp và gởi
  private static final ThreadLocal<byte[]> previous;
  // Độ phân giải và nén ảnh [0..1]
  private static final float JPG_QUALITY;
  
  static {
      previous = new ThreadLocal();
      JPG_QUALITY = 0.6f;
  }

  private final double scale;
  
  public ChupManHinh(double scale) {
    this.scale = scale;
  }

  public ChupManHinh() {
    this(1.0);
  }
  /**
   *  Chụp màn hình
   * @param robot Đối tượng thao tác event
   * @return object byte[]
   * @throws IOException 
   */
  public Object execute(Robot robot) throws IOException {
    // Tính thời gian gởi
    long time = System.currentTimeMillis();
    // Chụp màn hình
    Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
    Rectangle shotArea = new Rectangle(
        defaultToolkit.getScreenSize());
    BufferedImage image = robot.createScreenCapture(shotArea);
    // Thay đổi độ scale 
    if (scale != 1.0) {
      image = getScaledInstance(image);
    }
    // Chuyển anh sang JPG và bytes để gởi cho server
    byte[] bytes = convertToJPG(image);
    time = System.currentTimeMillis() - time;
    System.out.println("time = " + time);
    // Chỉ gởi nếu số byte thật sự thay đổi
    // Bằng cách so sánh với số byte trước đó
    byte[] prev = previous.get();
    if (prev != null && Arrays.equals(bytes, prev)) {
      return null;
    }
    // Thêm số byte lần chụp này để so sánh lần tiếp theo
    previous.set(bytes);
    return bytes;
  }

  private byte[] convertToJPG(BufferedImage img)
      throws IOException {
    ImageWriter writer =
        ImageIO.getImageWritersByFormatName("jpg").next();
    ImageWriteParam iwp = writer.getDefaultWriteParam();
    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    iwp.setCompressionQuality(JPG_QUALITY);
    
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    writer.setOutput(new MemoryCacheImageOutputStream(bout));
    writer.write(null, new IIOImage(img, null, null), iwp);
    writer.dispose();
    bout.flush();
    return bout.toByteArray();
  }

  public BufferedImage getScaledInstance(BufferedImage src) {
    int width = (int) (src.getWidth() * scale);
    int height = (int) (src.getHeight() * scale);

    Image scaled = src.getScaledInstance(width, height,
        BufferedImage.SCALE_AREA_AVERAGING);
    BufferedImage result = new BufferedImage(
        width, height, BufferedImage.TYPE_INT_RGB
    );
    result.createGraphics().drawImage(
        scaled, 0, 0, width, height, null);
    return result;
  }

  public String toString() {
    return "ScreenShot(" + scale + ")";
  }

  public boolean equals(Object o) {
    if (!(o instanceof ChupManHinh)) return false;
    return Double.compare(((ChupManHinh) o).scale, scale) == 0;
  }

  public int hashCode() {
    long temp = Double.doubleToLongBits(scale);
    return (int) (temp ^ (temp >>> 32));
  }
}