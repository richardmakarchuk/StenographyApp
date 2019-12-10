//import java.io.File;
//import java.io.BufferedWriter;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.io.PrintWriter;
//import java.util.Scanner;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;
//
//public class stegDecode {
//
//
//    public static String message = "";
//    public static int len = 0;
//
//    public static void main(String[] args) throws Exception {
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Enter image name");
//        String decodeImgName = keyboard.nextLine();
//
//        BufferedImage stegImg=readImageFile(decodeImgName);
//
//        DecodeTheMessage(stegImg);
//        String msg = "";
//        for(int i=0;i<len*8;i=i+8){
//
//            String subString= message.substring(i, i+8);
//
//            int m=Integer.parseInt(subString, 2);
//            char c=(char) m;
//            msg+=c;
//        }
//        PrintWriter out = new PrintWriter(new FileWriter("decodeMessage.txt", true), true);
//        out.write(msg);
//        out.close();
//    }
//    public static BufferedImage readImageFile(String coverImage){
//        BufferedImage stegImg = null;
//        File pic = new File (coverImage);
//        try{
//            stegImg = ImageIO.read(pic);
//        }catch (IOException e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//        return stegImg;
//    }
//
//
//    public static void DecodeTheMessage (BufferedImage stegImg) throws Exception{
//
//        int j = 0;
//        int currentBit = 0;
//        String msgString = "";
//        for (int x = 0; x < stegImg.getWidth(); x++){
//            for ( int y = 0; y < stegImg.getHeight(); y++){
//                if(x==0 && y<8){
//                    int currentPixel = stegImg.getRGB(x, y);
//                    int red = currentPixel>>16;
//                    red = red & 255;
//                    int green = currentPixel>>8;
//                    green = green & 255;
//                    int blue = currentPixel;
//                    blue = blue & 255;
//                    String blueString=Integer.toBinaryString(blue);
//                    msgString+=blueString.charAt(blueString.length()-1);
//                    len = Integer.parseInt(msgString, 2);
//                }
//                else if(currentBit < len*8){
//
//                    int currentPixel = stegImg.getRGB(x, y);
//                    int red = currentPixel>>16;
//                    red = red & 255;
//                    int green = currentPixel>>8;
//                    green = green & 255;
//                    int blue = currentPixel;
//                    blue = blue & 255;
//                    String blueString=Integer.toBinaryString(blue);
//                    message +=blueString.charAt(blueString.length()-1);
//                    currentBit++;
//                }
//            }
//        }
//    }
//}