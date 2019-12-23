package fr.theskinter.mcdreams.utils;

import java.util.LinkedList;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class UtilMath {
	public static double offset(Location paramLocation1, Location paramLocation2) { return offset(paramLocation1.toVector(), paramLocation2.toVector()); }
  
	public static double offset(Vector paramVector1, Vector paramVector2) { return paramVector1.subtract(paramVector2).length(); }
  
	public static final Random random = new Random(System.nanoTime());
  
	public static float randomRange(float paramFloat1, float paramFloat2) { return paramFloat1 + (float)Math.random() * (paramFloat2 - paramFloat1); }
  
	public static int randomRange(int paramInt1, int paramInt2) {
		Random localRandom = new Random();
		int i = localRandom.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
		return i;
	}
  
	public static double randomRange(double paramDouble1, double paramDouble2) {
		return Math.random() < 0.5D ? (1.0D - Math.random()) * (paramDouble2 - paramDouble1) + paramDouble1 : Math.random() * (paramDouble2 - paramDouble1) + paramDouble1;
	}
  
	public static double arrondi(double paramDouble, int paramInt) {
		return (int)(paramDouble * Math.pow(10.0D, paramInt) + 0.5D) / Math.pow(10.0D, paramInt);
	}
  
	public static int getRandomWithExclusion(int paramInt1, int paramInt2, int... paramVarArgs) {
		int i = paramInt1 + random.nextInt(paramInt2 - paramInt1 + 1 - paramVarArgs.length);
		for (int m : paramVarArgs) {
			if (i < m) {
				break;
			}
			i++;
		}
		return i;
	}

	public static float getLookAtYaw(Vector paramVector) {
		double d1 = paramVector.getX();
		double d2 = paramVector.getZ();
		double d3 = Math.atan2(d1, d2);
		return (float)d3 - 90.0F;
	}
  
	public static boolean elapsed(long paramLong1, long paramLong2) {
		return System.currentTimeMillis() - paramLong1 > paramLong2;
	}
  
	public static Vector getBumpVector(Entity paramEntity, Location paramLocation, double paramDouble) {
		Vector localVector = paramEntity.getLocation().toVector().subtract(paramLocation.toVector()).normalize();
		if (Double.isNaN(localVector.getX())) {
			localVector.setX(0);
		}
		if (Double.isNaN(localVector.getZ())) {
			localVector.setZ(0);
		}
		localVector.multiply(paramDouble);
		return localVector;
	}
  
	public static Vector getPullVector(Entity paramEntity, Location paramLocation, double paramDouble) {
		Vector localVector = paramLocation.toVector().subtract(paramEntity.getLocation().toVector()).normalize();
		localVector.multiply(paramDouble);
		return localVector;
	}
  
	public static void bumpEntity(Entity paramEntity, Location paramLocation, double paramDouble) {
		paramEntity.setVelocity(getBumpVector(paramEntity, paramLocation, paramDouble));
	}
  
	public static void bumpEntity(Entity paramEntity, Location paramLocation, double paramDouble1, double paramDouble2) {
		Vector localVector = getBumpVector(paramEntity, paramLocation, paramDouble1);
		localVector.setY(paramDouble2);
		paramEntity.setVelocity(localVector);
	}
  
	public static void pullEntity(Entity paramEntity, Location paramLocation, double paramDouble) {
		paramEntity.setVelocity(getPullVector(paramEntity, paramLocation, paramDouble));
	}
  
	public static void pullEntity(Entity paramEntity, Location paramLocation, double paramDouble1, double paramDouble2) {
		Vector localVector = getPullVector(paramEntity, paramLocation, paramDouble1);
		localVector.setY(paramDouble2);
		paramEntity.setVelocity(localVector);
  }

	public static final Vector rotateAroundAxisX(Vector paramVector, double paramDouble) {
		double d3 = Math.cos(paramDouble);
		double d4 = Math.sin(paramDouble);
		double d1 = paramVector.getY() * d3 - paramVector.getZ() * d4;
		double d2 = paramVector.getY() * d4 + paramVector.getZ() * d3;
		return paramVector.setY(d1).setZ(d2);
	}
  
	public static final Vector rotateAroundAxisY(Vector paramVector, double paramDouble) {
		double d3 = Math.cos(paramDouble);
		double d4 = Math.sin(paramDouble);
		double d1 = paramVector.getX() * d3 + paramVector.getZ() * d4;
		double d2 = paramVector.getX() * -d4 + paramVector.getZ() * d3;
		return paramVector.setX(d1).setZ(d2);
	}
  
	public static final Vector rotateAroundAxisZ(Vector paramVector, double paramDouble) {
		double d3 = Math.cos(paramDouble);
		double d4 = Math.sin(paramDouble);
		double d1 = paramVector.getX() * d3 - paramVector.getY() * d4;
		double d2 = paramVector.getX() * d4 + paramVector.getY() * d3;
		return paramVector.setX(d1).setY(d2);
	}
  
	public static final Vector rotateVector(Vector paramVector, double paramDouble1, double paramDouble2, double paramDouble3) {
		rotateAroundAxisX(paramVector, paramDouble1);
		rotateAroundAxisY(paramVector, paramDouble2);
		rotateAroundAxisZ(paramVector, paramDouble3);
		return paramVector;
	}
  
	public static Vector rotate(Vector paramVector, Location paramLocation) {
		double d1 = paramLocation.getYaw() / 180.0F * 3.141592653589793D;
		double d2 = paramLocation.getPitch() / 180.0F * 3.141592653589793D;
    
		paramVector = rotateAroundAxisX(paramVector, d2);
		paramVector = rotateAroundAxisY(paramVector, -d1);
		return paramVector;
	}
  
	public static byte toPackedByte(float paramFloat) {
		return (byte)(int)(paramFloat * 256.0F / 360.0F);
	}
  
	public static Vector getRandomVector() {
		double d1 = random.nextDouble() * 2.0D - 1.0D;
		double d2 = random.nextDouble() * 2.0D - 1.0D;
		double d3 = random.nextDouble() * 2.0D - 1.0D;
		return new Vector(d1, d2, d3).normalize();
	}

	public static Vector getRandomCircleVector() {
		double d1 = random.nextDouble() * 2.0D * 3.141592653589793D;
		double d2 = Math.cos(d1);
		double d3 = Math.sin(d1);
		double d4 = Math.sin(d1);
		return new Vector(d2, d4, d3);
	}

	public static final BlockFace[] axis = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };
	public static final byte[] axisByte = { 3, 4, 2, 5 };
  
	public static Vector getRandomVectorline() {
		int i = -5;
		int j = 5;
		int k = (int)(Math.random() * (j - i) + i);
		int m = (int)(Math.random() * (j - i) + i);
    
		double d1 = -5.0D;
		double d2 = -1.0D;
		double d3 = Math.random() * (d2 - d1) + d1;
    
		return new Vector(m, d3, k).normalize();
  }
  
	public static final Vector rotateVector(Vector paramVector, float paramFloat1, float paramFloat2) {
		double d1 = Math.toRadians(-1.0F * (paramFloat1 + 90.0F));
		double d2 = Math.toRadians(-paramFloat2);

		double d3 = Math.cos(d1);
		double d4 = Math.cos(d2);
		double d5 = Math.sin(d1);
		double d6 = Math.sin(d2);
    
		double d7 = paramVector.getX();
		double d8 = paramVector.getY();
		double d10 = d7 * d4 - d8 * d6;
		double d11 = d7 * d6 + d8 * d4;
    
		double d9 = paramVector.getZ();
		d7 = d10;
		double d12 = d9 * d3 - d7 * d5;
		d10 = d9 * d5 + d7 * d3;

		return new Vector(d10, d11, d12);
  }
  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static LinkedList<Vector> createCircle(double paramDouble1, double paramDouble2) {
		double d1 = paramDouble1 * paramDouble2;
		double d2 = 6.283185307179586D / d1;
		LinkedList localLinkedList = new LinkedList();
		for (int i = 0; i < d1; i++) {
			double d3 = i * d2;
			double d4 = paramDouble1 * Math.cos(d3);
			double d5 = paramDouble1 * Math.sin(d3);
			Vector localVector = new Vector(d4, 0.0D, d5);
			localLinkedList.add(localVector);
		}
		return localLinkedList;
	}
}