package rtracer;

/**
 * podtrieda stvorca so schopnostou odrazu
 * @author Sharkey
 *
 */
public class MirrorRect extends Rectangle {

	public Rectangle [][] reflSides;
	public double [] c2RGB = new double[3];
	public Line refl;
    
	public MirrorRect(Line _v1, double [] _v2) {
		super(_v1, _v2);
	}
	public MirrorRect(double [] p1, double [] p2, double [] p3) {
		super(p1, p2, p3);
	}
	public MirrorRect(boolean fromCam, double [] p1, double [] p2, double [] p3) {
		super(fromCam, p1, p2, p3);
	}
	/**
	 * nastavenie schopnosti prepisat farbu najdeneho objektu na seba v intervale <0,1>
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColor2(double r, double g, double b) {c2RGB[0] = r; c2RGB[1] = g; c2RGB[2] = b;}
	/**
	 * hladanie objektu na ktory by som narazil pri odraze od zrkadla a uprava farby v pripade uspechu
	 * @param u
	 * @param spheres
	 * @param C
	 * @return
	 */
    public int [] controlReflection(Line u, Sphere [] spheres, int [] C) {
		double length = 100;
		int [] okColor = null;
		for(int i = 0; i < reflSides.length; ++i) {
			for(int j = 0; j < reflSides[i].length; ++j) {
				double [] dN = reflSides[i][j].getIntersectionPoint(u);
				if(!rt.isOnWorld(dN[0]) || !rt.isOnWorld(dN[1]) || !rt.isOnWorld(dN[2])) continue;
				Line dv = u.pointTo(dN);
				if(Vector.isPointOnPolygon(dv.Y2, dv.X2, reflSides[i][j].Y, reflSides[i][j].X) && dv.getSize() < length) {
					length = dv.getSize();
					okColor = reflSides[i][j].c1RGB;
				}
			}
		}
		for(Sphere r: spheres) {
			double [] dN = r.getIntersectionPoint(u);
			if(dN != null) {
				Line dv = u.pointTo(dN);
				if(dv.getSize() < length) {
					length = dv.getSize();
					okColor = r.c1RGB;
				}
			}
		}
		if(okColor == null) return null;
		C[0] = (int)(C[0] * (1 - c2RGB[0]) + okColor[0] * c2RGB[0]);
		C[1] = (int)(C[1] * (1 - c2RGB[1]) + okColor[1] * c2RGB[1]);
		C[2] = (int)(C[2] * (1 - c2RGB[2]) + okColor[2] * c2RGB[2]);
		return C;
    }
}
