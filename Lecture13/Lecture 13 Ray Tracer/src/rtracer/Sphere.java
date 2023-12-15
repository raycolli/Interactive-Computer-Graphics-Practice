package rtracer;

/**
 * objekt gule...
 * @author Sharkey
 *
 */
public class Sphere {
	Vector middle;
	double radius;
	double pradius;
	public int [] c1RGB;
	boolean isOver = false;
	public String name;

	public int minX, minY, maxX, maxY;

	public Sphere(double [] v, double r) {
		middle = new Vector(v);
		setRadius(r);
		c1RGB = new int [3];
		c1RGB[0] = 0;
		c1RGB[1] = 0;
		c1RGB[2] = 0;
	}
	public String toString() { return name; }
	
	/**
	 * hladanie bodu dopadu vektora na gulu podla kvadratickej rovnice..
	 * @param w
	 * @return
	 */
	public double [] getIntersectionPoint(Line w) {
		double dX = w.X - middle.X;
		double dY = w.Y - middle.Y;
		double dZ = w.Z - middle.Z;
		double [] N = Vector.norm(w.v);
		double A = Vector.scalarProduct(N[0], N[1], N[2], N[0], N[1], N[2]);
		double B = Vector.scalarProduct(dX, dY, dZ, N[0], N[1], N[2]) * 2;
		double C = Vector.scalarProduct(dX, dY, dZ, dX, dY, dZ) - (pradius*pradius);
		double D = B*B - 4 * A * C;
		if(D < 0) return null;
		double t = (-B - Math.sqrt(D)) / ( 2 * A);
		double [] res = {w.X + t*N[0], w.Y + t*N[1], w.Z + t*N[2]};
		return res;
	}
	/**
	 * vypocet polomeru a jeho zmenenej velkosti vzhladom na vzdialenost gule od kamery
	 * @param d
	 */
	public void setRadius(double d) {
		pradius = d;
		double pX = Line.getProjectedX(d, middle.Z) - Line.getProjectedX(0, middle.Z);
		double dX = Line.getProjectedX(d, 0) - Line.getProjectedX(0, 0);
		radius = d * (pX / dX);
		makeProjection();
	}
	public int getProjectedX() {
		return Line.getProjectedX(middle.X, middle.Z);
	}
	public int getProjectedY() {
		return Line.getProjectedY(middle.Y, middle.Z);
	}
	public int getProjectedRadius() {
		return Line.getProjectedX(middle.X-pradius, middle.Z);
	}
	public void moveToX(double d) {
		middle.X = d;
		makeProjection();
	}
	public void moveToY(double d) {
		middle.Y = d;
		makeProjection();
	}
	public void moveToZ(double d) {
		middle.Z = d;
		makeProjection();
	}
	public void setColor(int r, int g, int b) {c1RGB[0] = r; c1RGB[1] = g; c1RGB[2] = b; }
	public void makeProjection() {
		int dX = getProjectedX();
		int dY = getProjectedY();
		int dR = dX - getProjectedRadius();
		minY = dY - dR;
		maxY = dY + dR;
		minX = dX - dR;
		maxX = dX + dR;
		if(minX < 0) minX = 0;
		if(maxX > Line.wLength) maxX = (int)Line.wLength;
		if(minY < 0) minY = 0;
		if(maxY > Line.wLength) maxY = (int)Line.wLength;
	}
}
