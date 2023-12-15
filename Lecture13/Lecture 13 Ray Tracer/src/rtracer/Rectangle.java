package rtracer;
/**
 * objekt stvorca 
 * @author Sharkey
 *
 */
public class Rectangle {
	public Line v1;
	public Line v2;
	public Line v3;
	
	public int X1; public int Y1;
	public int X2; public int Y2;
	public int X3; public int Y3;
	public int X4; public int Y4;
	
	public int [] X = new int[4];
	public int [] Y = new int[4];
	
	public String name;

	public double [] midPoint;

	public int [] c1RGB;
	
	public double [] N;

	public String toString() { return name; }
	public Rectangle(Line _v1, double [] _v2) {
		v1 = _v1;
		v2 = new Line(_v1.getP1(), _v2);
		v3 = Line.sum(v1, v2);
		N = Vector.norm(rtracer.Vector.vectorProduct(v1, v2));
		makeProjection();
		initColors();
	}
	public Rectangle(double [] p1, double [] p2, double [] p3) {
		v1 = new Line(p1, p2);
		v2 = new Line(p1, p3);
		v3 = Line.sum(v1, v2);
		N = Vector.norm(rtracer.Vector.vectorProduct(v1, v2));
		makeProjection();
		initColors();
	}
	public Rectangle(boolean fromCam, double [] p1, double [] p2, double [] p3) {
		v1 = new Line(fromCam, p1, p2);
		v2 = new Line(fromCam, p1, p3);
		v3 = Line.sum(v1, v2);
		N = Vector.norm(Vector.vectorProduct(v1, v2));
		makeProjection();
		initColors();
	}
	private void initColors() {
		c1RGB = new int[3];
		c1RGB[0] = 0;
		c1RGB[1] = 0;
		c1RGB[2] = 0;
	}

	/**
	 * nastavenie farby stvorca
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor(int r, int g, int b) {c1RGB[0] = r; c1RGB[1] = g; c1RGB[2] = b; }
	/**
	 * volanie rotacie vektorov definujucich stvorec
	 * @param angle
	 */
	public void rorateByX(double angle) { v1.rotateByX(angle); v2.rotateByX(angle); makeProjection(); }
	public void rorateByY(double angle) { v1.rotateByY(angle); v2.rotateByY(angle); makeProjection(); }
	public void rorateByZ(double angle) { v1.rotateByZ(angle); v2.rotateByZ(angle); makeProjection(); }
	
	/**
	 * vypocty suradnic stvorca po projektii
	 */
	protected void makeProjection() {
		X[0] = X1 = v1.X1;
		Y[0] = Y1 = v1.Y1;
		X[1] = X2 = v1.X2;
		Y[1] = Y2 = v1.Y2;
		X[3] = X3 = v2.X2;
		Y[3] = Y3 = v2.Y2;
		X[2] = X4 = v3.X2;
		Y[2] = Y4 = v3.Y2;
		double [] m1 = v1.getP1();
		double [] m2 = v1.getP2();
		double [] m3 = v2.getP2();
		double [] m4 = v3.getP2();
		midPoint = new double[3];
		midPoint[0] = (m1[0] + m2[0] + m3[0] + m4[0]) / 4;
		midPoint[1] = (m1[1] + m2[1] + m3[1] + m4[1]) / 4;
		midPoint[2] = (m1[2] + m2[2] + m3[2] + m4[2]) / 4;
	}
	
	/**
	 * kontrola, ci vybrane 4 body tvoria stvorec
	 * @obsolete
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 */
	public static boolean isRect(double [] p0, double [] p1, double [] p2, double [] p3) {
		Line t = Line.sum(new Line(true, p0, p1), new Line(true, p0, p2));
		if(t.getP2()[0] == p3[0] && t.getP2()[1] == p3[1] && t.getP2()[2] == p3[2]) return true;
		return false;
	}
	/**
	 * vzdialenost bodu od stredu stvorca
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double getDistanceFrom(double  x, double y, double z) {
		return (N[0]*(x-midPoint[0]) + N[1]*(y-midPoint[1]) + N[2]*(z-midPoint[2])) / Math.sqrt(N[0]*N[0] + N[1]*N[1] + N[2]*N[2]);
	}
	public double getDistanceFrom(double [] w) {
		return getDistanceFrom(w[0], w[1], w[2]);
	}
	public double getDistanceFrom(Line w) {
		return getDistanceFrom(w.X, w.Y, w.Z);
	}
	/**
	 * vypocet suradnic bodu po predlzeni do roviny efinovanej stvorcom
	 * @param w
	 * @return
	 */
	public double [] getIntersectionPoint(Line w) {
		double t = (N[0]*(midPoint[0]-w.X) + N[1]*(midPoint[1]-w.Y) + N[2]*(midPoint[2]-w.Z)) / (N[0]*w.v.X + N[1]*w.v.Y + N[2]*w.v.Z);
		double [] res = {w.X + t*w.v.X, w.Y + t*w.v.Y, w.Z + t*w.v.Z};
		return res;
	}
}
