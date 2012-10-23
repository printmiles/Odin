/* Class name: BarcodeGenerator
 * File name:  BarcodeGenerator.java
 * Project:    Open Document and Information Network (Odin)
 ** Copyright:  © 2007-2012 Alexander J. Harris, all rights reserved
 * Created:    19-Jul-2011 09:38:26
 * Modified:   19-Jul-2011
 *
 * Version History:
 * ~ ~ ~ ~ ~ ~ ~ ~ ~
 * 0.001  19-Jul-2011 Initial build
 */
package odin.sleipnir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

/**
 * JavaDoc to follow. Currently in ALPHA
 * @version 0.001
 * @author Alexander J. Harris (email: Alexander.J.Harris(at)btinternet.com)
 */
public class BarcodeGenerator
{
  /**
   * This is a test method and NOT to be used in production
   */
  public void testGenerateQRCode()
  {
    // FIXME Remove this method before putting into production
    String text = "98376373783"; // this is the text that we want to encode

    int width = 400;
    int height = 300; // change the height and width as per your requirement

    // (ImageIO.getWriterFormatNames() returns a list of supported formats)
    String imageFormat = "png"; // could be "gif", "tiff", "jpeg" 

    try
    {
      BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
      MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, new FileOutputStream(new File("qrcode_97802017507991.png")));
    }
    catch (WriterException wX)
    {
      wX.printStackTrace();
    }
    catch (IOException ioX)
    {
      ioX.printStackTrace();
    }
  }
}
