package fr.theskinter.mcdreams.objects;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import fr.theskinter.mcdreams.utils.UtilMath;
import lombok.Getter;

public class Cuboid {
	
		private Location pointA;
		private Location pointB;
		@Getter public int xMin;
		@Getter public int xMax;
		@Getter public int yMin;
		@Getter public int yMax;
		@Getter public int zMin;
		@Getter public int zMax;
		@Getter public World world;
		public List<Location> locationsSquared;
	
	public Cuboid(Location paramLocation1, Location paramLocation2) {
		this.xMin = Math.min(paramLocation1.getBlockX(), paramLocation2.getBlockX());
		this.xMax = Math.max(paramLocation1.getBlockX(), paramLocation2.getBlockX());
		this.yMin = Math.min(paramLocation1.getBlockY(), paramLocation2.getBlockY());
		this.yMax = Math.max(paramLocation1.getBlockY(), paramLocation2.getBlockY());
		this.zMin = Math.min(paramLocation1.getBlockZ(), paramLocation2.getBlockZ());
		this.zMax = Math.max(paramLocation1.getBlockZ(), paramLocation2.getBlockZ());
		this.world = paramLocation1.getWorld();
		
		this.locationsSquared = getHollowCube(paramLocation1, paramLocation2, 1.0D);
		
	}
	
	public Cuboid() {}
	
	public Cuboid build() {
		this.xMin = Math.min(this.pointA.getBlockX(), this.pointB.getBlockX());
		this.xMax = Math.max(this.pointA.getBlockX(), this.pointB.getBlockX());
		this.yMin = Math.min(this.pointA.getBlockY(), this.pointB.getBlockY());
		this.yMax = Math.max(this.pointA.getBlockY(), this.pointB.getBlockY());
		this.zMin = Math.min(this.pointA.getBlockZ(), this.pointB.getBlockZ());
		this.zMax = Math.max(this.pointA.getBlockZ(), this.pointB.getBlockZ());
		this.world = this.pointA.getWorld();
		
		this.locationsSquared = getHollowCube(this.pointA, this.pointB, 1.0D);
		return this;
	}
	
	public Cuboid setPointA(Location paramLocation) {
		if ((this.pointB != null) && (!this.pointB.getWorld().equals(paramLocation.getWorld()))) {
			return null;
		}
		this.pointA = paramLocation;
		return this;
	}
	
	public Cuboid setPointB(Location paramLocation) {
		if ((this.pointA != null) && (!this.pointA.getWorld().equals(paramLocation.getWorld()))) {
			return null;
		}
		this.pointB = paramLocation;
		return this;
	}
	
	public boolean isIn(Location paramLocation) {
		if (paramLocation.getWorld() != this.world) {
			return false;
		}
		if (paramLocation.getBlockX() < this.xMin) {
			return false;
		}
		if (paramLocation.getBlockX() > this.xMax) {
			return false;
		}
		if (paramLocation.getBlockY() < this.yMin) {
			return false;
		}
		if (paramLocation.getBlockY() > this.yMax) {
			return false;
		}
		if (paramLocation.getBlockZ() < this.zMin) {
			return false;
		}
		if (paramLocation.getBlockZ() > this.zMax) {
			return false;
		}
		return true;
	}
	
	public Vector getMinimumPoint() {
		return new Vector(this.xMin, this.yMin, this.zMin);
	}
	
	public Vector getMaximumPoint() {
		return new Vector(this.xMax, this.yMax, this.zMax);
	}
	
	public Block[] corners() {
		Block[] arrayOfBlock = new Block[8];
		World localWorld = this.world;
		arrayOfBlock[0] = localWorld.getBlockAt(this.xMin, this.yMin, this.zMin);
		arrayOfBlock[1] = localWorld.getBlockAt(this.xMin, this.yMin, this.zMax);
		arrayOfBlock[2] = localWorld.getBlockAt(this.xMin, this.yMax, this.zMin);
		arrayOfBlock[3] = localWorld.getBlockAt(this.xMin, this.yMax, this.zMax);
		arrayOfBlock[4] = localWorld.getBlockAt(this.xMax, this.yMin, this.zMin);
		arrayOfBlock[5] = localWorld.getBlockAt(this.xMax, this.yMin, this.zMax);
		arrayOfBlock[6] = localWorld.getBlockAt(this.xMax, this.yMax, this.zMin);
		arrayOfBlock[7] = localWorld.getBlockAt(this.xMax, this.yMax, this.zMax);
		return arrayOfBlock;
	}
	
	public int getLength() {
		Vector localVector1 = getMinimumPoint();
		Vector localVector2 = getMaximumPoint();
		
		return (int)(localVector2.getZ() - localVector1.getZ() + 1.0D);
	}
	
	public List<Location> getHollowCube(Location paramLocation1, Location paramLocation2, double paramDouble) {
		ArrayList<Location> localArrayList = new ArrayList<>();
		World localWorld = paramLocation1.getWorld();
		double d1 = Math.min(paramLocation1.getX(), paramLocation2.getX());
		double d2 = Math.min(paramLocation1.getY(), paramLocation2.getY());
		double d3 = Math.min(paramLocation1.getZ(), paramLocation2.getZ());
		double d4 = Math.max(paramLocation1.getX(), paramLocation2.getX());
		double d5 = Math.max(paramLocation1.getY(), paramLocation2.getY());
		double d6 = Math.max(paramLocation1.getZ(), paramLocation2.getZ());
		for (double d7 = d1; d7 <= d4; d7 += paramDouble) {
			for (double d8 = d2; d8 <= d5; d8 += paramDouble) {
				for (double d9 = d3; d9 <= d6; d9 += paramDouble) {
					int i = 0;
					if ((d7 == d1) || (d7 == d4)) {
						i++;
					}
					if ((d8 == d2) || (d8 == d5)) {
						i++;
					}
					if ((d9 == d3) || (d9 == d6)) {
						i++;
					}
					if (i >= 2) {
						localArrayList.add(new Location(localWorld, d7, d8, d9));
					}
				}
			}
		}
		return localArrayList;
			}
	
	public List<Location> getLocationsSquared() {
		return this.locationsSquared;
	}
	
	public Location getCenter() {
		int i = this.xMax + 1;
		int j = this.yMax + 1;
		int k = this.zMax + 1;
		return new Location(this.world, this.xMin + (i - this.xMin) / 2.0D, this.yMin + (j - this.yMin) / 2.0D, this.zMin + (k - this.zMin) / 2.0D);
	}
	
	public Location getPointB() {
		return this.pointB;
	}
	
	public Location getPointA() {
		return this.pointA;
	}
	
	public int getXWidth() {
		return this.xMax - this.xMin;
	}
	
	public int getZWidth() {
		return this.zMax - this.zMin;
	}
	
	public int getHeight() {
		return this.yMax - this.yMin;
	}
	
	public int getArea() {
		return getHeight() * getXWidth() * getZWidth();
	}
	
	public Location getRandomFromCenter() {
		return new Location(getCenter().getWorld(), UtilMath.randomRange(this.xMin, this.xMax), UtilMath.randomRange(this.yMin, this.yMax), UtilMath.randomRange(this.zMin, this.zMax));
	}
}