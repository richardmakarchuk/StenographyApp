//import java.io.File;
//import java.io.IOException;
//import java.io.FileNotFoundException;
//import java.util.Scanner;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;
//
//public class stegEncode {
//    public static void main(String[] args) throws Exception {
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Enter image name");
//        String imgName = keyboard.nextLine();
//        System.out.println("Enter Message to encode");
//        String messageToEncode = keyboard.nextLine();
//
//        int[] bits = bitMessage(messageToEncode);
//        BufferedImage hideImage = readImageFile(imgName);
//        hideMessage(bits, hideImage);
//    }
//
//    public static int[] bitMessage(String msg){
//        int x = 0;
//        int[] bitMsg = new int[msg.length()*8];
//        for(int i = 0; i < msg.length(); i++){
//            int y = msg.charAt(i);
//            String xString = Integer.toBinaryString(y);
//            while(xString.length()!=8){
//                xString = '0' + xString;
//            }
//            for(int j = 0; j < 8; j++){
//                bitMsg[x] = Integer.parseInt(String.valueOf(xString.charAt(j)));
//                x++;
//            }
//        }
//        return bitMsg;
//    }
//    //read image
//    public static BufferedImage readImageFile(String imgName){
//        BufferedImage hideIamge = null;
//        File pic = new File (imgName);
//        try{
//            hideIamge = ImageIO.read(pic);
//        }catch(IOException e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//        return hideIamge;
//    }
//    //hide image in blue pixel
//    public static void hideMessage (int[] bits, BufferedImage hideImage) throws Exception{
//        Scanner keyboard = new Scanner(System.in);
//        System.out.println("Enter steg image name");
//        String stegImgName = keyboard.nextLine();
//        File f = new File(stegImgName);
//        BufferedImage stegImg = null;
//        int bitLength = bits.length/8;
//        int[] bitMsg = new int[8];
//        String bitString = Integer.toBinaryString(bitLength);
//
//        while(bitString.length() != 8){
//            bitString = '0' + bitString;
//        }
//
//        for(int i = 0; i < 8; i++){
//            bitMsg[i] = Integer.parseInt(String.valueOf(bitString.charAt(i)));
//        };
//        int j = 0;
//        int b = 0;
//        int currentBit = 8;
//        for(int x = 0; x < hideImage.getWidth(); x++){
//            for(int y = 0; y < hideImage.getHeight(); y++){
//                if(x == 0 && y < 8){
//                    //get current pixel
//                    int currentPixel = hideImage.getRGB(x, y);
//                    //break pixel up to aRGB
//                    int red = currentPixel>>16;
//                    red = red & 255;
//                    int green = currentPixel>>8;
//                    green = green & 255;
//                    int blue = currentPixel;
//                    blue = blue & 255;
//
//                    //hide message in blue pixel
//                    String blueString = Integer.toBinaryString(blue);
//                    String blueSubString = blueString.substring(0, blueString.length()-1);
//                    blueSubString = blueSubString + Integer.toString(bitMsg[b]);
//
//                    //replace blue pixel
//                    int temp = Integer.parseInt(blueSubString, 2);
//                    int bluePixel = Integer.parseInt(blueSubString, 2);
//                    int a = 255;
//
//                    int rgb = (a<<24) | (red<<16) | (green<<8) | bluePixel;
//
//                    hideImage.setRGB(x, y, rgb);
//                    ImageIO.write(hideImage, "png", f);
//                    b++;
//                }
//                else if (currentBit < bits.length + 8){
//                    int currentPixel = hideImage.getRGB(x, y);
//                    int red = currentPixel >> 16;
//                    red = red & 255;
//                    int green = currentPixel >> 8;
//                    green = green & 255;
//                    int blue = currentPixel;
//                    blue = blue & 255;
//
//                    String blueString = Integer.toBinaryString(blue);
//                    String blueSubString = blueString.substring(0, blueString.length()-1);
//                    blueSubString = blueSubString + Integer.toString(bits[j]);
//                    j++;
//                    int temp = Integer.parseInt(blueSubString, 2);
//                    int bluePixel = Integer.parseInt(blueSubString, 2);
//                    int a = 255;
//                    int rgb = (a<<24) | (red<<16) | (green<<8) | bluePixel;
//
//                    hideImage.setRGB(x, y, rgb);
//                    ImageIO.write(hideImage, "png", f);
//                    currentBit++;
//                }
//            }
//        }
//    }
//}