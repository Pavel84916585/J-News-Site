package RBPO.RBPO.security;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class GoogleAuthenticator {

    public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        try                                                                                                                     {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }


    public static void createQRCode(String barCodeData, String filePath, int height, int width)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
                width, height);


        try (FileOutputStream out = new FileOutputStream(filePath)) {
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        }
    }


    public static String getBase64QRCode(String barCodeData) {

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, 400, 400);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "png", outputStream);

            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
