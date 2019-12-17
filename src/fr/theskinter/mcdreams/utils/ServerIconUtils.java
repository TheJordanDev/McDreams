package fr.theskinter.mcdreams.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

@SuppressWarnings("unused")
public class ServerIconUtils {

	public static CachedServerIcon getIcon() throws Exception {
		return Bukkit.loadServerIcon(blank());
	}
	
	
	private static BufferedImage imageFromFile(File file) {
		try { return ImageIO.read(file); } 
		catch (IOException e) { return null; }
		
	}
	
	private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
	private static File getBackupIcon(File file,BufferedImage image) {
        File aFile = file;
        String path = file.getParent();
        int fileNo = 0;
        if (aFile.exists() && !aFile.isDirectory()) {
            while(aFile.exists()){
                fileNo++;
                aFile = new File(path+"/backup_icon(" + fileNo + ").png");
            }
        }
        return aFile;
    }
    
	private static File getLastBackupIcon(File file) {
        File aFile = file;
        String path = file.getParent();
        int fileNo = 0;
        if (aFile.exists() && !aFile.isDirectory()) {
            //newFileName = filename.replaceAll(getFileExtension(filename), "(" + fileNo + ")" + getFileExtension(filename));

            while(aFile.exists()){
                fileNo++;
                aFile = new File(path+"/backup_icon(" + fileNo + ").png");
            }
            if (fileNo-1 == 0) {
            	aFile = null;
            } else {
            	aFile = new File(path+"/backup_icon(" + (fileNo-1) + ").png");
            }
            System.out.println(fileNo);

        } else if (!aFile.exists()) {
            return file;
        }
        return aFile;
    }
    
	private boolean compareImage(BufferedImage fileA, BufferedImage fileB) {

        float percentage = 0;
        BufferedImage biA = fileA;
        DataBuffer dbA = biA.getData().getDataBuffer();
        int sizeA = dbA.getSize();
        BufferedImage biB = fileB;
        DataBuffer dbB = biB.getData().getDataBuffer();
        int sizeB = dbB.getSize();
        int count = 0;
        if (sizeA == sizeB) {
        	for (int i = 0; i < sizeA; i++) {
        		if (dbA.getElem(i) == dbB.getElem(i)) {
        			count = count + 1;
        		}
        	}
        	percentage = (count * 100) / sizeA;
        } else {
        	System.out.println("Both the images are not of same size");
        }
        return (percentage==1);
    }
	
	private static BufferedImage blank() { return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB); }
}
