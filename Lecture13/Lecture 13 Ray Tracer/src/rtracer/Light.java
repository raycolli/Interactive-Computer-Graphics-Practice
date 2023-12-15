package rtracer;

/**
 * osamostatnenie svetla ako samostatneho objektu so snahou implementovat viac svetiel
 * (nestihol som ;-)
 * @author Sharkey
 *
 */
public class Light {
	public Line lightRay;
	public Rectangle [][] lightSides;
	
	public Light(Line v) {
		lightRay = v;
	}
	/**
	 * hladanie objektu leziaceho v usecke definovanej vektorom svetla
	 * @param r
	 * @param spheres
	 * @return
	 */
    public boolean controlShadow(Rectangle r, Sphere [] spheres) {
		for(int i = 0; i < lightSides.length; ++i) {
			for(int j = 0; j < lightSides[i].length; ++j) {
				double [] dN = lightSides[i][j].getIntersectionPoint(lightRay);
				if(!rt.isOnWorld(dN[0]) || !rt.isOnWorld(dN[1]) || !rt.isOnWorld(dN[2])) continue;
				Line dv = lightRay.pointTo(dN);
				if(Vector.isPointOnPolygon(dv.Y2, dv.X2, lightSides[i][j].Y, lightSides[i][j].X) && lightRay.getSize() > dv.getSize()) {
					return true;
				}
			}
		}
		for(Sphere sr: spheres) {
			if(sr.getIntersectionPoint(lightRay) != null) {
				return true;
			}
		}
    	return false;
    }	
}
