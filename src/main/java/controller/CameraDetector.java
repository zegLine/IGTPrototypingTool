package controller;

import com.sun.jna.Native;

public class CameraDetector {

    static {
        Native.register("avicap32");
    }

    public static native int capGetDriverDescriptionA(int wDriverIndex, byte[] lpszName, int cbName, byte[] lpszVer, int cbVer);

    public static void main(String[] args) {
       detectCamera();
    }

    public static String detectCamera() {
        String cameraName = "No cameras detected";
        int driverCount = capGetDriverDescriptionA(0, null, 0, null, 0);
        if (driverCount > 0) {
            System.out.println("Cameras detected:");
            for (int i = 0; i < driverCount; i++) {
                byte[] name = new byte[100];
                byte[] version = new byte[100];
                capGetDriverDescriptionA(i, name, 100, version, 100);
                System.out.println("Camera " + (i + 1) + ": " + new String(name).trim());
                cameraName = "Camera " + (i + 1) + ": " + new String(name).trim();
            }

            return cameraName;} else {
            System.out.println("No cameras detected.");
        } return cameraName;
    }
}
