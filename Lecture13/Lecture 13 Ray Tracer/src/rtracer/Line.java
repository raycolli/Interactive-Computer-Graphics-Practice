package rtracer;

/**
 * objekt ciary s par silno potrebnymi konstantami
 * @author Sharkey
 *
 */
public class Line {
    static final double base = 16;
    static final double max = 36;
    static final double wLength = base * max;
    static final double minBase = base / 2;
    static final double def = (max / 2) / (((max+1)*max) / 2);

	public double X, Y, Z;

	public Vector v;
	
	public double [] N;
	
	public int X1 = 0;
	public int Y1 = 0;
	public int X2 = 0;
	public int Y2 = 0;

	/**
	 * pocet N po sebe udicich cisel(pouziva sa pri projekcii)
	 * @param x
	 * @return
	 */
	public static double getSum(double x) { return ((x+1)*x) / 2; }
	
	public Line() {}
	public Line(double x1, double y1, double z1, double x2, double y2, double z2) {
		setP(x1, y1, z1);
		v = new Vector(x2, y2, z2);
		makeProjection();
	}
	public Line(double[] p, double[] w) {
		this.setP(p);
		v = new Vector(w);
		makeProjection();
	}
	public Line(boolean camToPoint, double [] p, double [] w) {
		double [] d = {w[0] - p[0], w[1] - p[1], w[2] - p[2]};
		this.setP(p);
		v = new Vector(d);
		makeProjection();
	}

	/**
	 * nastavenie pociatocneho bodu
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setP(double x, double y, double z) {X = x; Y = y; Z = z; } 
	public void setP (double [] w) { if(w.length == 3) setP(w[0], w[1], w[2]); };
	/**
	 * nastavenie vektora, ktory tvori velkost usecky
	 * @param w
	 */
	public void setP2(double [] w) { if(w.length == 3) v.setVector(w); };

	/**
	 * 3D suradnice pociatocneho bodu
	 * @return
	 */
	public double [] getP1() { double [] tmp = {X, Y, Z}; return tmp; }
	/**
	 * 3D suradnice koncoveho bodu
	 * @return
	 */
	public double [] getP2() {
		double [] tmp = {X + v.X, Y + v.Y, Z + v.Z};
		return tmp;
	}

	public double getP2X()  { return X + v.X; }
	public double getP2Y()  { return Y + v.Y; }
	public double getP2Z()  { return Z + v.Z; }

	public double getSize() { return v.size; }

	/**
	 * vypocet suctu 2 vektorov s rovnakym pociatocnym bodom pre dopocitanie 4. bodu stvorca
	 * @param a
	 * @param b
	 * @return
	 */
	public static Line sum(Line a, Line b) {
		double [] p = {a.v.X + b.v.X, a.v.Y + b.v.Y, a.v.Z + b.v.Z};
		return new Line(a.getP1(), p);
	}
	
	/**
	 * rotacia vektora podla osi X
	 * @param angle
	 */
	public void rotateByX(double angle) {
		double size = ((double)Math.round(Math.sqrt((v.Z * v.Z) + (v.Y * v.Y))*1000)/1000);
		double newAngle = ((Math.PI / 180)*angle) + Math.asin(v.Y / size);
		if(size == 0 || angle == 0) return;
		v.setVectorZ((double)Math.floor((size * Math.cos(newAngle))*1000)/1000);
		v.setVectorY((double)Math.floor((size * Math.sin(newAngle))*1000)/1000);
		makeProjection();
	}
	/**
	 * rotacia vektora podla osi Y
	 * @param angle
	 */
	public void rotateByY(double angle) {
		double size = ((double)Math.round(Math.sqrt((v.X * v.X) + (v.Z * v.Z))*1000)/1000);
		double newAngle = ((Math.PI / 180)*angle) + Math.asin(v.Z / size);
		System.out.println(size+" "+angle+" "+newAngle+" "+(v.X)+" "+v.Z+" "+Math.floor((size * Math.cos(newAngle))*1000)/1000);
		if(size == 0 || angle == 0) return;
		v.setVectorX((double)Math.floor((size * Math.cos(newAngle))*1000)/1000);
		v.setVectorZ((double)Math.floor((size * Math.sin(newAngle))*1000)/1000);
		makeProjection();
	}
	/**
	 * rotacia vektora podla osi Z
	 * @param angle
	 */
	public void rotateByZ(double angle) {
		double size = ((double)Math.round(Math.sqrt((v.X * v.X) + (v.Y * v.Y))*1000)/1000);
		double newAngle = ((Math.PI / 180)*angle) + Math.asin(v.X / size);
		if(size == 0 || angle == 0) return;
		v.setVectorX((double)Math.floor((size * Math.sin(newAngle))*1000)/1000);
		v.setVectorY((double)Math.floor((size * Math.cos(newAngle))*1000)/1000);
		makeProjection();
	}
	
	/**
	 * vypocet Xovej suradnice v 2D podla Xovej a Zovej suradnice v 3D
	 * @param x
	 * @param z
	 * @return
	 */
	public static int getProjectedX(double x, double z) {
		double dz = z - (getSum(z) * def);
		return (int)Math.floor((x + ((dz * minBase) + ((1 - (dz / max)) * x * (base-1)))));
	}
	/**
	 * vypocet Yovej suradnice v 2D podla Yovej a Zovej suradnice v 3D
	 * @param x
	 * @param z
	 * @return
	 */
	public static int getProjectedY(double y, double z) {
		double dz = z - (getSum(z) * def);
		return (int)Math.floor((y - ((dz * minBase) + ((1 - (dz / max)) * y * (base+1))) + wLength));
	}
	/**
	 * metavypocet suradnic usecky
	 */
	public void makeProjection() {
		X1 = getProjectedX(X,       Z);
		Y1 = getProjectedY(Y,       Z);
		X2 = getProjectedX(X + v.X, Z + v.Z);
		Y2 = getProjectedY(Y + v.Y, Z + v.Z);
	}

	/**
	 * zoradenie vektorov podla velkosti
	 * @param toSort
	 * @return
	 */
    public static Line [] sortVectors(Line [] toSort) {
    	boolean changed = true;
    	Line [] tmp = toSort;
    	Line t;
    	do {
    		changed = false;
    		for(int i = 0; i < tmp.length-1; i++) {
    			if(tmp[i+1].getSize() < tmp[i].getSize()) {
    				t = tmp[i+1];
    				tmp[i+1] = tmp[i];
    				tmp[i] = t;
    				changed = true;
    			}
    		}
    	}
    	while(changed);
    	return tmp;
    }
    /**
     * zoradenie pola stvorcov podla pola vektorov, vyuziva sa pri prvotnom hladani najblizich
     * stvorcov od bodu kamery alebo svetla
     * @param toSort
     * @param sortBy
     * @return
     */
    public static Rectangle [] sortRectanglesByArrayByVectors(Rectangle [] toSort, Line [] sortBy) {
    	boolean changed = true;
    	Line [] tmp = sortBy;
    	Line t;
    	Rectangle [] tmp2 = toSort;
    	Rectangle t2;
    	do {
    		changed = false;
    		for(int i = 0; i < tmp.length-1; i++) {
    			if(tmp[i+1].getSize() < tmp[i].getSize()) {
    				t = tmp[i+1];
    				tmp[i+1] = tmp[i];
    				tmp[i] = t;
    				t2 = tmp2[i+1];
    				tmp2[i+1] = tmp2[i];
    				tmp2[i] = t2;
    				changed = true;
    			}
    		}
    	}
    	while(changed);
    	return tmp2;
    }
    /**
     * zoradenie kociek podla vzdialenosti od kamery, vyuzivalo sa pri snahe minimalizovat pocet
     * testov vektora luca pri raytracingu
     * @param _c
     * @return
     */
    public static Cube [] sortCubesByDepth(Cube [] _c) {
    	boolean changed = true;
    	Cube tmp [] = _c;
    	Cube t;
    	do {
    		changed = false;
    		for(int i = 0; i < tmp.length-1; i++) {
    			if(tmp[i+1].depth < tmp[i].depth) {
    				t = tmp[i+1];
    				tmp[i+1] = tmp[i];
    				tmp[i] = t;
    				changed = true;
    			}
    		}
    	}
    	while(changed);
    	return tmp;
    }

    /**
     * zmena vektora luca aby smeroval do vybraneho bodu
     * @param p
     * @return
     */
	public Line pointTo(double [] p) {
		double [] tmp = {X, Y, Z};
		return new Line(true, tmp, p);
	}
}


