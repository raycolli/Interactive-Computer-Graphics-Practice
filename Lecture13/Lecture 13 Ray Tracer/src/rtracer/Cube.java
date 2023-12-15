package rtracer;

/**
 * objekt kocky
 * @author Sharkey
 *
 */
public class Cube {
	public Line [] v = new Line[7];
	public int        X1, X2, X3, X4, X5, X6, X7, X8;
	public int        Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8;
	private double [] m1, m2, m3, m4, m5, m6, m7, m8;
	public Rectangle [] r = new Rectangle[6];
	
	public int minX, minY, maxX, maxY;
	
	public double angleX = 0, angleY = 0, angleZ = 0;
	
	public boolean isOver;
	
	public double [] midPoint = new double[3];
	
	public double depth;
	
	public String name;

	public int cR = 0;
	public int cG = 0;
	public int cB = 0;
	
	Rectangle [] visibleSides;

	public String toString() { return name; }
	/**
	 * inicializacia a vypocet vsetkych potrebnych vektorov pre najdenie bodov kocky
	 * definovej 3 vektormi
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 */
	public Cube(double [] p1, double [] p2, double [] p3, double [] p4) {
		isOver = false;
		v[0] = new Line(p1, p2);
		v[1] = new Line(p1, p3);
		v[2] = new Line(p1, p4);
		v[3] = Line.sum(v[0], v[1]);
		v[4] = Line.sum(v[0], v[2]);
		v[5] = Line.sum(v[1], v[2]);
		v[6] = Line.sum(v[3], v[2]);
		makeProjection();
	}
	/**
	 * rotacie vektorov kocky podla X
	 * @param angle
	 */
	public void rotateByX(double angle) {
		double d = angle - angleX;
		angleX = angle; 
		for(int i = 0; i < 7; ++i)
			v[i].rotateByX(-d); 
		makeProjection(); 
	}
	/**
	 * rotacie vektorov kocky podla Y
	 * @param angle
	 */
	public void rotateByY(double angle) { 
		double d = angle - angleY;
		angleY = angle;
		for(int i = 0; i < 7; ++i)
			v[i].rotateByY(-d);
		makeProjection(); 
	}
	/**
	 * rotacie vektorov kocky podla Z
	 * @param angle
	 */
	public void rotateByZ(double angle) { 
		double d = angle - angleZ;
		angleZ = angle; 
		for(int i = 0; i < 7; ++i)
			v[i].rotateByZ(-d);
		makeProjection(); 
	}
	/**
	 * posun vektorov kocky podla X
	 * @param d
	 */
	public void moveToX(double d) {
		for(int i = 0; i < 7; ++i)
			v[i].X = d; 
		makeProjection();
	}
	/**
	 * posun vektorov kocky podla Y
	 * @param d
	 */
	public void moveToY(double d) {
		for(int i = 0; i < 7; ++i)
			v[i].Y = d; 
		makeProjection();
	}
	/**
	 * posun vektorov kocky podla Z
	 * @param d
	 */
	public void moveToZ(double d) {
		for(int i = 0; i < 7; ++i)
			v[i].Z = d;
		makeProjection();
	}
	/**
	 * zmena velkosti vektorov kocky podla X
	 * @param d
	 */
	public void resizeByX(double d) {
		v[0].v.X = d;
		v[3] = Line.sum(v[0], v[1]);
		v[4] = Line.sum(v[0], v[2]);
		v[5] = Line.sum(v[1], v[2]);
		v[6] = Line.sum(v[3], v[2]);
		makeProjection();
	}
	/**
	 * zmena velkosti vektorov kocky podla Y
	 * @param d
	 */
	public void resizeByY(double d) {
		v[1].v.Y = d;
		v[3] = Line.sum(v[0], v[1]);
		v[4] = Line.sum(v[0], v[2]);
		v[5] = Line.sum(v[1], v[2]);
		v[6] = Line.sum(v[3], v[2]);
		makeProjection();
	}
	/**
	 * zmena velkosti vektorov kocky podla Z
	 * @param d
	 */
	public void resizeByZ(double d) {
		v[2].v.Z = d;
		v[4] = Line.sum(v[0], v[2]);
		v[5] = Line.sum(v[1], v[2]);
		v[6] = Line.sum(v[3], v[2]);
		makeProjection();
	}
	/**
	 * zmena hodnoty cervenej farby
	 * @param d
	 */
	public void changeColorR(int d) {
		cR = d;
		for(int i = 0; i < 6; ++i)
			r[i].c1RGB[0] = d;
	}
	/**
	 * zmena hodnoty zelenej farby
	 * @param d
	 */
	public void changeColorG(int d) {
		cG = d;
		for(int i = 0; i < 6; ++i)
			r[i].c1RGB[1] = d;
	}
	/**
	 * zmena hodnoty modrej farby
	 * @param d
	 */
	public void changeColorB(int d) {
		cB = d;
		for(int i = 0; i < 6; ++i)
			r[i].c1RGB[2] = d;
	}
	public void setColor(int _r, int _g, int _b) {
		cR = _r; cG = _g; cB = _b;
		for(int i = 0; i < 6; ++i)
			r[i].setColor(cR, cG, cB);
	}
	public int [] getColor() {int [] tmp = {cR, cG, cB}; return tmp; }
	
	private void makeProjection() {
		minX = 10000;
		minY = 10000;
		maxX = -10000;
		maxY = -10000;
		for(int i = 0; i < 7; ++i)
			v[i].makeProjection(); 
		X1 = v[0].X1; if(X1 < minX) minX = X1; if(X1 > maxX) maxX = X1;
		Y1 = v[0].Y1; if(Y1 < minY) minY = Y1; if(Y1 > maxY) maxY = Y1;
		X2 = v[0].X2; if(X2 < minX) minX = X2; if(X2 > maxX) maxX = X2;
		Y2 = v[0].Y2; if(Y2 < minY) minY = Y2; if(Y2 > maxY) maxY = Y2;
		X3 = v[1].X2; if(X3 < minX) minX = X3; if(X3 > maxX) maxX = X3;
		Y3 = v[1].Y2; if(Y3 < minY) minY = Y3; if(Y3 > maxY) maxY = Y3;
		X4 = v[3].X2; if(X4 < minX) minX = X4; if(X4 > maxX) maxX = X4;
		Y4 = v[3].Y2; if(Y4 < minY) minY = Y4; if(Y4 > maxY) maxY = Y4;
		X5 = v[2].X2; if(X5 < minX) minX = X5; if(X5 > maxX) maxX = X5;
		Y5 = v[2].Y2; if(Y5 < minY) minY = Y5; if(Y5 > maxY) maxY = Y5;
		X6 = v[4].X2; if(X6 < minX) minX = X6; if(X6 > maxX) maxX = X6;
		Y6 = v[4].Y2; if(Y6 < minY) minY = Y6; if(Y6 > maxY) maxY = Y6;
		X7 = v[5].X2; if(X7 < minX) minX = X7; if(X7 > maxX) maxX = X7;
		Y7 = v[5].Y2; if(Y7 < minY) minY = Y7; if(Y7 > maxY) maxY = Y7;
		X8 = v[6].X2; if(X8 < minX) minX = X8; if(X8 > maxX) maxX = X8;
		Y8 = v[6].Y2; if(Y8 < minY) minY = Y8; if(Y8 > maxY) maxY = Y8;
		if(minX < 0) minX = 0;
		if(maxX > Line.wLength) maxX = (int)Line.wLength;
		if(minY < 0) minY = 0;
		if(maxY > Line.wLength) maxY = (int)Line.wLength;
		m1 = v[0].getP1();
		m2 = v[0].getP2();
		m3 = v[1].getP2();
		m4 = v[3].getP2();
		m5 = v[2].getP2();
		m6 = v[4].getP2();
		m7 = v[5].getP2();
		m8 = v[6].getP2();
		r[0] = new Rectangle(true, m1, m2, m3);
		r[1] = new Rectangle(true, m1, m2, m5);
		r[2] = new Rectangle(true, m1, m3, m5);
		r[3] = new Rectangle(true, m2, m4, m6);
		r[4] = new Rectangle(true, m5, m6, m7);
		r[5] = new Rectangle(true, m3, m4, m7);
		for(int i = 0; i < 6; ++i)
			r[i].setColor(cR, cG, cB);
		
		midPoint[0] = (m1[0] + m2[0] + m3[0] + m4[0] + m5[0] + m6[0] + m7[0] + m8[0]) / 8;
		midPoint[1] = (m1[1] + m2[1] + m3[1] + m4[1] + m5[1] + m6[1] + m7[1] + m8[1]) / 8;
		midPoint[2] = (m1[2] + m2[2] + m3[2] + m4[2] + m5[2] + m6[2] + m7[2] + m8[2]) / 8;
	}
	/**
	 * najdenie najblizsich 3 stran kocky, ktore su viditelne z vektora, kedze vzdy vidime najviac
	 * 3 strany kocky
	 * @param cam
	 */
    public void setVisibleSides(Line cam) {
    	Line [] s = {
    			cam.pointTo(r[0].midPoint), 
    			cam.pointTo(r[1].midPoint), 
    			cam.pointTo(r[2].midPoint), 
    			cam.pointTo(r[3].midPoint), 
    			cam.pointTo(r[4].midPoint), 
    			cam.pointTo(r[5].midPoint)
    	};
		r = Line.sortRectanglesByArrayByVectors(r, s);
		visibleSides = new Rectangle[3];
		visibleSides[0] = r[0];
		visibleSides[1] = r[1];
		visibleSides[2] = r[2];
    }
}
