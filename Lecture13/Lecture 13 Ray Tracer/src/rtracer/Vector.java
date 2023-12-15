package rtracer;

/**
 * objekt vektora...alfa a omega celeho tohoto tu...
 * @author Sharkey
 *
 */
public class Vector {

	public double X,Y,Z;
	
	public double size;

	public Vector(double x, double y, double z) {
		setVector(x, y, z);
	}
	public Vector(double[] v) {
		setVector(v);
	}
	public void setVector (double x, double y, double z) { X = x; Y = y; Z = z; setSize(); };
	public void setVector (double [] v) { if(v.length == 3) X = v[0]; Y = v[1]; Z = v[2]; setSize(); }
	public void setVectorX(double    x) { X = x; setSize(); }
	public void setVectorY(double    y) { Y = y; setSize(); }
	public void setVectorZ(double    z) { Z = z; setSize(); }

	public double [] getVector () { double [] tmp = {X, Y, Z}; return tmp; }

	/**
	 * velkost vektora podla pytagorovej vety...
	 */
	private void setSize() { size = Math.sqrt((X * X) + (Y * Y) + (Z * Z)); };

	public void addToVector (double [] m) { X += m[0]; Y += m[1]; Z += m[2]; }
	public void addToVectorX(double    x) { X += x; }
	public void addToVectorY(double    y) { Y += y; }
	public void addToVectorZ(double    z) { Z += z; }
	
	public void multiplyVector(double m) {X *= m; Y *= m; Z *= m; }
	
	/**
	 * vektorovy sucin...casto najditelny aj ako crossProduct v inych zdrojoch...
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static Vector vectorProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
		double [] w = {y1*z2 - y2*z1, z1*x2 - z2*x1, x1*y2 - x2*y1};
		return new Vector(w);
	}
	public static Vector vectorProduct(double [] v1, double [] v2) {
		return vectorProduct(v1[0], v1[1], v1[2], v2[0], v2[1], v2[2]);
	}
	public static Vector vectorProduct(Line v1, Line v2) {
		return vectorProduct(v1.v.getVector(), v2.v.getVector());
	}
	/**
	 * normovanie vektora na 1...
	 * @param vX
	 * @param vY
	 * @param vZ
	 * @return
	 */
	public static double [] norm(double vX, double vY, double vZ) {
		double jX = Math.abs(vX);
		double jY = Math.abs(vY);
		double jZ = Math.abs(vZ);
		double max = jX;
		if(jY > max) max = jY;
		if(jZ > max) max = jZ;
		double [] res = {vX / max, vY / max, vZ / max};
		return res;
	}
	public static double [] norm(double [] _v) {
		return norm(_v[0], _v[1], _v[2]);
	}
	public static double [] norm(Vector _v) {
		return norm(_v.X, _v.Y, _v.Z);
	}
	/**
	 * skalarny sucin vektorov, zname skor ako dotProduct
	 * @param uX
	 * @param uY
	 * @param uZ
	 * @param vX
	 * @param vY
	 * @param vZ
	 * @return
	 */
	public static double scalarProduct(double uX, double uY, double uZ, double vX, double vY, double vZ) {
		return uX*vX + uY*vY + uZ*vZ;
	}
	public static double scalarProduct(double [] v1, double [] v2) {
		return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
	}
	public static int sign(double val) {
		if(val == 0) return 0;
		if(val > 0) return 1;
		return -1;
	}
	/**
	 * podla nejakej rovnice lezi bod v konvexnom polygone ak su znamienka determinantov matic
	 * definovanych ako suradnice 2 po sebe iducich bodov v polygone a suradnic hladaneho bodu
	 * totozne...kvoli problemom so stavom kde sa mali spajat 2 rozne stvorce v kocke ignorujem
	 * nulu, inak vznikali vsade prazdne ciary na mieste hran stvorcov
	 * @param _y
	 * @param _x
	 * @param pY
	 * @param pX
	 * @return
	 */
	public static boolean isPointOnPolygon(double _y, double _x, int [] pY, int [] pX) {
		if(pX.length != pY.length) return false;
		int s = pY.length;
		int oldA = 2;
		if(pX[0] == pX[1] && pX[0] == pX[2] && pX[0] == pX[3]) { //ak su vsetky body v jednej rovine, kontrolujem ci je aspon v intervale krajnych bodov..
			int minY = pY[0], maxY = pY[0];
			if(pY[1] < minY) minY = pY[1];
			if(pY[2] < minY) minY = pY[2];
			if(pY[3] < minY) minY = pY[3];
			if(pY[1] > maxY) maxY = pY[1];
			if(pY[2] > maxY) maxY = pY[2];
			if(pY[3] > maxY) maxY = pY[3];
			if(_x == pX[0] && _y > minY && _y < maxY) return true;
			return false;
		}
		else if(pY[0] == pY[1] && pY[0] == pY[2] && pY[0] == pY[3]) {
			int minX = pX[0], maxX = pX[0];
			if(pX[1] < minX) minX = pX[1];
			if(pX[2] < minX) minX = pX[2];
			if(pX[3] < minX) minX = pX[3];
			if(pX[1] > maxX) maxX = pX[1];
			if(pX[2] > maxX) maxX = pX[2];
			if(pX[3] > maxX) maxX = pX[3];
			if(_y == pY[0] && _x > minX && _x < maxX) return true;
			return false;
		}
		else {
			for(int i = 0; i < s; ++i) {
				double a = pX[i]*pY[(i+1)%s] + pY[i]*_x + pX[(i+1)%s]*_y - pY[(i+1)%s]*_x - pY[i]*pX[(i+1)%s] - pX[i]*_y;
				if(oldA != 2 && sign(a) != oldA && sign(a) != 0) return false;
				if(sign(a) != 0)
					oldA = sign(a);
			}
			return true;
		}
	}
}
