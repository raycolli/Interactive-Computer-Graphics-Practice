package rtracer;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import com.sun.j3d.utils.applet.MainFrame;
/**
 * hlavna app celeho tohoto cuda s nazvom raytracer..:)
 * @author Sharkey
 *
 */
public class rt extends Applet implements MouseListener, MouseMotionListener, AdjustmentListener, ActionListener {

	private static final long serialVersionUID = 1L;
	static MainFrame mf;
	public static double [] nul = {0,0,0};

	public Cube      [] cubes      = new Cube     [4];
	public Rectangle [] rectangles = new Rectangle[5];
	public Sphere    [] spheres    = new Sphere   [2];

	public Line cam;
	public Light L1;

	PixelMap [][] im;

	Object obj, selObj;

	Scrollbar [][] cubeScrolls   = new Scrollbar[4][3];
	Scrollbar [][] sphereScrolls = new Scrollbar[3][3];
	Label     [][] Label         = new Label    [4][4];
	Button traceIt, addCube, addSphere, removeIt;

	public Graphics g;
	/**
	 * kontrola double bodu, ci je v imaginarnom svete vektorov
	 * @param point
	 * @return
	 */
	public static boolean isOnWorld(double point) {
		if(point < 0 || point > Line.max) return false;
		return true;
	}
	/**
	 * kontrola int bodu, ci je v pixelovom svete 
	 * @param point
	 * @return
	 */
	public static boolean isOnWorld(int point) {
		if(point < 0 || point > Line.wLength) return false;
		return true;
	}

	/**
	 * vytvorenie popisoveho labela pre kategoriu zmien
	 * @param i
	 * @param text
	 * @param top
	 */
	public void makeBigLabel(int i, String text, int top) {
		Label[i][0] = new Label(text);
		Label[i][0].setBackground(new Color(0, 0, 0));
		Label[i][0].setForeground(new Color(255, 255, 255));
		Label[i][0].setBounds(585, top, 200, 12);
		add(Label[i][0]);
	}
	/**
	 * vytvorenie maleho popisu k scrollbarom
	 * @param i
	 * @param j
	 * @param text
	 * @param top
	 */
	public void makeLabel(int i, int j, String text, int top) {
		Label[i][j] = new Label(text);
		Label[i][j].setBackground(new Color(0, 0, 0));
		Label[i][j].setForeground(new Color(255, 255, 255));
		Label[i][j].setBounds(585, top, 15, 12);
		add(Label[i][j]);
	}
	/**
	 * scrollbar pre objekt kocky
	 * @param i
	 * @param j
	 * @param top
	 * @param val
	 * @param max
	 */
	public void makeCubeScroll(int i, int j, int top, int val, int max) {
		cubeScrolls[i][j] = new Scrollbar(Scrollbar.HORIZONTAL, val, 1, 0, max);
		cubeScrolls[i][j].setBounds(600, top, 200, 15);
		cubeScrolls[i][j].addAdjustmentListener(this);
		add(cubeScrolls[i][j]);
	}
	/**
	 * len volanie scrollbaru a jeho popisu pre kocku
	 * @param i
	 * @param j
	 * @param top
	 * @param text
	 * @param val
	 * @param max
	 */
	public void makeCubeBox(int i, int j, int top, String text, int val, int max) {
		makeCubeScroll(i, j, top, val, max);
		makeLabel(i, j+1, text, top+1);
	}
	/**
	 * scrollbar pre objekt gule
	 * @param i
	 * @param j
	 * @param top
	 * @param val
	 * @param max
	 */
	public void makeSphereScroll(int i, int j, int top, int val, int max) {
		sphereScrolls[i][j] = new Scrollbar(Scrollbar.HORIZONTAL, val, 1, 0, max);
		sphereScrolls[i][j].setBounds(600, top, 200, 15);
		sphereScrolls[i][j].addAdjustmentListener(this);
		add(sphereScrolls[i][j]);
	}
	/**
	 * len volanie scrollbaru a jeho popisu pre gulu
	 * @param i
	 * @param j
	 * @param top
	 * @param text
	 * @param val
	 * @param max
	 */
	public void makeSphereBox(int i, int j, int top, String text, int val, int max) {
		makeSphereScroll(i, j, top, val, max);
		makeLabel(i, j+1, text, top+1);
	}
	/**
	 * odstranenie labelov a scrollerov
	 */
	public void removeForm() {
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 4; ++j) {
				if(Label[i][j] != null) remove(Label[i][j]);
			}
		}
		for(int i = 0; i < 4; ++i) {
			for(int j = 0; j < 3; ++j) {
				if(cubeScrolls[i][j] != null) remove(cubeScrolls[i][j]);
			}
		}
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				if(sphereScrolls[i][j] != null) remove(sphereScrolls[i][j]);
			}
		}
		if(removeIt != null) remove(removeIt);
	}
	
	/**
	 * vykreslenie hran kocky po projekcii
	 * @param c
	 * @param cc
	 */
	public void drawCube(Cube c, Color cc) {
		g.setColor(cc);
		g.drawLine(c.X1, c.Y1, c.X2, c.Y2);
		g.drawLine(c.X1, c.Y1, c.X3, c.Y3);
		g.drawLine(c.X3, c.Y3, c.X4, c.Y4);
		g.drawLine(c.X4, c.Y4, c.X2, c.Y2);
		g.drawLine(c.X1, c.Y1, c.X5, c.Y5);
		g.drawLine(c.X2, c.Y2, c.X6, c.Y6);
		g.drawLine(c.X5, c.Y5, c.X6, c.Y6);
		g.drawLine(c.X3, c.Y3, c.X7, c.Y7);
		g.drawLine(c.X4, c.Y4, c.X8, c.Y8);
		g.drawLine(c.X7, c.Y7, c.X8, c.Y8);
		g.drawLine(c.X5, c.Y5, c.X7, c.Y7);
		g.drawLine(c.X6, c.Y6, c.X8, c.Y8);
	}
	/**
	 * vykreslenie hran kocky s definovanou farbou ak bola predtym oznacena
	 * @obsolete
	 * @param c
	 */
	public void drawCube(Cube c) {
		drawCube(c, (c.isOver) ? new Color(0, 255, 0) : new Color(255, 0, 0));
	}
	/**
	 * vykreslenie vsetkych kociek(vyuzivalo sa na zaciatku pri testoch
	 * @obsolete
	 */
	public void drawCubes() {
		for(Cube c: cubes) {
			drawCube(c);
		}
	}
	/**
	 * vykreslenie kruznice opisujucej gulu po projekcii
	 * @param r
	 * @param cc
	 */
	public void drawSphere(Sphere r, Color cc) {
		g.setColor(cc);
		int dX = r.getProjectedX();
		int dY = r.getProjectedY();
		int dR = dX - r.getProjectedRadius();
		g.drawOval(dX-dR, dY-dR, dR*2, dR*2);
	}
	/**
	 * vykreslenie kruznice s definovanou farbou podla parametra oznacenia
	 * @obsolete
	 * @param r
	 */
	public void drawSphere(Sphere r) {
		drawSphere(r, (r.isOver) ? new Color(0, 255, 0) : new Color(255, 0, 0));
	}
	/**
	 * vykreslenie vsetkych kruznic
	 * @obsolete
	 */
	public void drawSpheres() {
		for(Sphere r: spheres) {
			drawSphere(r);
		}
	}
	/**
	 * vykreslenie stvorcovej siete sveta na zaciatku behu appletu
	 */
	public void drawWorld() {
		Line v;
		int _max = (int)(Line.max);
		g.setColor(new Color(0, 0, 255));
		for(int i = 0; i <= _max; i++) {
			//bottom side
			v = new Line(0,    0,    i, _max, 0, 0   ); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			v = new Line(i,    0,    0, 0,    0, _max); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			//top side
			v = new Line(0,    _max, i, _max, 0, 0   ); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			v = new Line(i,    _max, 0, 0,    0, _max); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			//left side
			v = new Line(0,    0,    i, 0, _max, 0   ); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			v = new Line(0,    i,    0, 0, 0,    _max); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			//right side
			v = new Line(_max, 0,    i, 0, _max, 0   ); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
			v = new Line(_max, i,    0, 0, 0,    _max); g.drawLine(v.X1, v.Y1, v.X2, v.Y2);
		}
	}
	/**
	 * vykreslenie casti PixelMap-y pri prekreslovani obrysov vybraneho objektu
	 * @param minY
	 * @param maxY
	 * @param minX
	 * @param maxX
	 */
	public void drawImagePart(int minY, int maxY, int minX, int maxX) {
		g = getGraphics();
		for(int i = minY; i <= maxY; ++i) {
			for(int j = minX; j <= maxX; ++j) {
				if(im[i][j] == null) continue;
				g.setColor(new Color(im[i][j].getR(), im[i][j].getG(), im[i][j].getB()));
				g.drawLine(j, i, j, i);
			}
		}
	}
	
	/**
	 * vygenerovanie objektu kocky s pociatkom na (x,y,z), velkostou(w,h,l), narocenim(rx,ry,rz) a farbou(r,g,b)
	 * @param id
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @param h
	 * @param d
	 * @param rx
	 * @param ry
	 * @param rz
	 * @param r
	 * @param g
	 * @param b
	 */
	public void generateCube(int id,
			double x,  double y,  double z,
			double w,  double h,  double d, 
			double rx, double ry, double rz,
			int r,     int g,     int b) {
		double [] u = {x, y, z}; 
		double [] u1 = {w, 0, 0};
		double [] u2 = {0, h, 0};
		double [] u3 = {0, 0, d};
		cubes[id] = new Cube(u, u1, u2, u3);
		if(rx != 0) cubes[id].rotateByX(rx);
		if(ry != 0) cubes[id].rotateByY(ry);
		if(rz != 0) cubes[id].rotateByZ(rz);
		cubes[id].setColor(r, g, b);
		cubes[id].setVisibleSides(cam);
		cubes[id].name = "cube "+id;
	}
	/**
	 * vygenerovanie zakladnych kociek na zaciatku behu appletu
	 */
	public void generateCubes() {
		generateCube(0, 2,  0,  8, 8,  8, 7,  0, 0, 0, 0,   255, 0);
		generateCube(1, 13, 0, 16, 8,  8, 7,  0, 0, 0, 255, 0,   0);
		generateCube(2, 24, 0, 14, 8,  8, 7,  0, 0, 0, 0,   0,   255);
		generateCube(3, 5,  0,  3, 16, 2, 2,  0, 0, 0, 255, 0,   255);
		setSides();
	}
	/**
	 * vytvorenie stvorca s vlastnostou odrazu definovanom bodmi(a1,a2,a3), farbou(r,g,b)
	 * schopnostou drzat si farbu po odraze(r2,g2,b2) v intervale <0,1> a pomocnym
	 * bodom p pre vytvorenie vektora pri hladani vyditelnych hran kociek zo zrkadla
	 * @param id
	 * @param a1
	 * @param a2
	 * @param a3
	 * @param r
	 * @param g
	 * @param b
	 * @param r2
	 * @param g2
	 * @param b2
	 * @param p
	 */
	public void generateRect(int id, double [] a1, double [] a2, double [] a3, int r, int g, int b, double r2, double g2, double b2, double [] p) {
		rectangles[id] = new MirrorRect(a1, a2, a3); //left
		rectangles[id].setColor(r, g, b);
		((MirrorRect)rectangles[id]).setColor2(r2, g2, b2);
		((MirrorRect)rectangles[id]).refl = new Line(p, nul);
	}
	/**
	 * vytvorenie obycajneho stvorca definovaneho bodmi(a1,a2,a3) a farbou (r,g,b)
	 * @param id
	 * @param a1
	 * @param a2
	 * @param a3
	 * @param r
	 * @param g
	 * @param b
	 */
	public void generateRect(int id, double [] a1, double [] a2, double [] a3, int r, int g, int b) {
		rectangles[id] = new Rectangle(a1, a2, a3); //left
		rectangles[id].setColor(r, g, b);
	}
	/**
	 * vytvorenie sveta ohraniceneho stvorcami
	 */
	public void generateRects() {
		double [] reflP = {0, (int)Line.max/2, 0};
		double [] reflP2 = {(int)Line.max, (int)Line.max/2, 0};
		double [] p1 = {Line.max,0,0};
		double [] p2 = {0,Line.max,0};
		double [] p3 = {0,0,Line.max};
		generateRect(0, nul, p3, p1, 0, 255, 255);                        //bottom
		generateRect(1, nul, p3, p2, 0, 255, 255, 0.5, 0.5, 0.5, reflP);  //left
		generateRect(2, p2,  p3, p1, 0, 255, 255);                        //top
		generateRect(3, p1,  p3, p2, 0, 255, 255, 0.5, 0.5, 0.5, reflP2); //right
		generateRect(4, p3,  p1, p2, 0, 255, 255);                        //back
	}
	/**
	 * generovanie rule definovanej stredovym bodom a, polomerom rad a farbou (r,g,b)
	 * @param id
	 * @param a
	 * @param rad
	 * @param r
	 * @param g
	 * @param b
	 */
	public void generateSphere(int id, double [] a, double rad, int r, int g, int b) {
		spheres[id] = new Sphere(a, rad);
		spheres[id].setColor(r, g, b);
		spheres[id].name = "sphere "+id;
	}
	/**
	 * inicializacne vytvorenie gul na zaciatku behu appletu
	 */
	public void generateSpheres() {
		double [] tmp1 = {18, 22, 22};
		double [] tmp2 = {6, 10, 12};
		generateSphere(0, tmp1, 5, 255, 255, 0);
		generateSphere(1, tmp2, 2, 255, 0, 255);
	}

	/**
	 * vygenerovanie novej kocky s nahodnymi parametrami do sveta
	 */
	public void addCube() {
		int l = cubes.length;
		Cube [] c = new Cube[l+1];
		for(int i = 0; i < l; ++i)
			c[i] = cubes[i];
		cubes = c;
		generateCube(l, 
				(double)Math.round(Math.random()*10)+10,
				(double)Math.round(Math.random()*10)+1,
				(double)Math.round(Math.random()*5)+1,
				(double)Math.round(Math.random()*3)+3,
				(double)Math.round(Math.random()*3)+3,
				(double)Math.round(Math.random()*3)+3,
				(double)0, (double)0, (double)0,
				(int)Math.round(Math.random()*255),
				(int)Math.round(Math.random()*255),
				(int)Math.round(Math.random()*255)
				);
		setSides();
	}
	/**
	 * vygenerovanie novej gule s nahodnymi parametrami do sveta
	 */
	public void addSphere() {
		int l = spheres.length;
		Sphere [] c = new Sphere[l+1];
		for(int i = 0; i < l; ++i)
			c[i] = spheres[i];
		spheres = c;
		double [] tmp = {(double)Math.round(Math.random()*10)+10, (double)Math.round(Math.random()*10)+1, (double)Math.round(Math.random()*5)+1};
		generateSphere(l, 
				tmp, 
				(double)Math.round(Math.random()*3)+1,
				(int)Math.round(Math.random()*255),
				(int)Math.round(Math.random()*255),
				(int)Math.round(Math.random()*255)
				);
	}
	/**
	 * odstranenie objektu, ktory bol vybrany pri upravach sveta
	 */
	public void removeObject() {
		if(selObj instanceof Cube) {
			Cube [] c = new Cube[cubes.length-1];
			int j = 0;
			for(int i = 0; i < cubes.length; ++i) {
				if(cubes[i].equals(selObj)) continue;
				c[j] = cubes[j];
				j++;
			}
			cubes = c;
			setSides();
		}
		if(selObj instanceof Sphere) {
			Sphere [] c = new Sphere[spheres.length-1];
			int j = 0;
			for(int i = 0; i < spheres.length; ++i) {
				if(spheres[i].equals(selObj)) continue;
				c[j] = spheres[j];
				j++;
			}
			spheres = c;
		}
	}
	
	/**
	 * nastavenie viditelnych stran pre zrkadla a svetlo pre zrychlenie vypoctov
	 */
	public void setSides() {
		L1.lightSides = new Rectangle[cubes.length][3];

		for(int i = 0; i < cubes.length; ++i) {
			Rectangle [] vs = cubes[i].visibleSides;
			cubes[i].setVisibleSides(L1.lightRay);
			L1.lightSides[i] = cubes[i].visibleSides;
			for(Rectangle r: rectangles) {
				if(r instanceof MirrorRect) {
					if(((MirrorRect)r).reflSides == null || ((MirrorRect)r).reflSides.length != cubes.length)
						((MirrorRect)r).reflSides = new Rectangle[cubes.length][3];
					cubes[i].setVisibleSides(((MirrorRect)r).refl);
					((MirrorRect)r).reflSides[i]  = cubes[i].visibleSides;
				}
			}
			cubes[i].visibleSides = vs;
		}
	}

	/**
	 * inicializacne vytvorenie svetla, sveta, kocik a gul
	 */
	public void init() {
		double [] tmp = {Line.max/2, Line.max/2, -Line.max/2};
		double [] tmp2 = {Line.max/2, Line.max, Line.max/2};
		cam = new Line(tmp, nul);

		L1 = new Light(new Line(tmp2, nul));

		generateRects();
		generateCubes();
		generateSpheres();

		addMouseListener( this );
		addMouseMotionListener( this );
	}
	/**
	 * vykreslenie bud nakresu sveta, alebo jeho vizualu ak uz bol aspon raz volany rtrace
	 */
	public void paint( Graphics g ) {
		this.g = g;
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, 850, 590);
		if(im != null) {
			for(int i = 0; i < im.length; ++i) {
				for(int j = 0; j < im[i].length; ++j) {
					if(im[j][i] != null) {
						g.setColor(new Color(im[j][i].getR(), im[j][i].getG(), im[j][i].getB()));
						g.drawLine(i, j, i, j);
					}
				}
			}
		}
		else {
			drawWorld();
			drawCubes();
			drawSpheres();
		}
		if(traceIt == null) {
			traceIt = new Button("RayTrace!");
			traceIt.setBounds(590, 500, 100, 20);
			traceIt.addActionListener(this);
			add(traceIt);
			addCube = new Button("Add Cube");
			addCube.setBounds(590, 525, 100, 20);
			addCube.addActionListener(this);
			add(addCube);
			addSphere = new Button("Add Sphere");
			addSphere.setBounds(590, 550, 100, 20);
			addSphere.addActionListener(this);
			add(addSphere);
		}
	}

	/**
	 * samotne volanie lucov potencionalne do kazdeho bodu vo svete
	 * (bola snaha a testy o prvotnu projekciu a tym zabezpecit dopad na kazdy bod
	 * boli neuspesne kvoli velmi dlhemu casu pocitania)
	 */
	public void rtrace() {
		this.g = getGraphics();
		im = new PixelMap[(int)Line.wLength+1][(int)Line.wLength+1];
		double [] tmp = {0, 0, 0};
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, 850, 650);
		for(double k = 0.1; k >= 0.033; k-=0.033) {
			for(tmp[0] = 0; tmp[0] < Line.max; tmp[0]+=k) {
				for(tmp[1] = 0; tmp[1] < Line.max; tmp[1]+=k) {
					fireRay(cam.pointTo(tmp));
				}
			}
		}
	}

	/**
	 * samotne pokusy luca o dopad na niektoru z viditelnych stran kociek,
	 * do oblasti gule alebo do oblasti stvorcov ohranicujucich svet
	 * @param ray
	 */
	public void fireRay(Line ray) {
		for(Cube c: cubes)
			for(Rectangle r: c.visibleSides)
				fireRayToRect(ray, r, c);

		for(Sphere r: spheres)
			fireRayToSphere(ray, r);

		for(Rectangle r: rectangles)
			fireRayToRect(ray, r, r);

		return;
	}
	/**
	 * kontrola luca ray pre dopad na stvorec r kocky o
	 * @param ray
	 * @param r
	 * @param o
	 */
	public void fireRayToRect(Line ray, Rectangle r, Object o) {
		double [] point = r.getIntersectionPoint(ray); //suradnice bodu na pozicii predlzeneho vektora do roviny definovanej vektorami stvorca

		if(!isOnWorld(point[0]) || !isOnWorld(point[1]) || !isOnWorld(point[2])) return; // ak je mimo sveta, prec
		ray = ray.pointTo(point); // samotne predlzenie vektora do najdeneho bodu

		if(!isOnWorld(ray.X2) || !isOnWorld(ray.Y2)) return; // osetrenie ci vektor po projekcii naozaj nevyletel zo sveta
		if(!Vector.isPointOnPolygon(ray.Y2, ray.X2, r.Y, r.X)) return; // osetrenie, ci vektor dopadajuci na rovinu je v oblasti stvorca
		if(r.c1RGB[0] == 0 && r.c1RGB[1] == 0 && r.c1RGB[2] == 0) return; // pre vykreslenie  musi mat stvorec definovanu nejaku farbu

		if(im[ray.Y2][ray.X2] == null) im[ray.Y2][ray.X2] = new PixelMap(ray.X2, ray.Y2, ray.getSize()); // vytvorenie bodu v pixelmape
		else if(im[ray.Y2][ray.X2].depth < ray.getSize()) return; // pripad ked uz definovany bod je blizsie ku kamere nez novy vektor(kvoli prekryvaniu objektov)

		im[ray.Y2][ray.X2].depth = ray.getSize(); // doplnenie novej vzdialenosti ak sme nasli neskor blizi bod po projektii

		double [] E = Vector.norm(ray.v); 
		double S = Vector.scalarProduct(E, r.N) * (-2);
		Line u = new Line(ray.getP2X(), ray.getP2Y(), ray.getP2Z(), (S*r.N[0])+E[0], (S*r.N[1])+E[1], (S*r.N[2])+E[2]);
		// podla nejakeho vzorca najdeneho googlom vytvorenie vektora, ktory vznikne odrazom zo storca 

		L1.lightRay = L1.lightRay.pointTo(ray.getP2()); // vzdialenost od svetla (bohuzial implementovat dynamicky viac bodov osvetlenia som nestihol
		int size = (int)Math.abs((L1.lightRay.getSize()-20)*6); 
		// kedze sa vzdialenost od lampy pohybovala vzhladom k svetu v okoli 20ky,
		// takouto divnou rovnicou clovek najde pre mna optimalnu zmenu farby podla vzdialenosti
		int [] nC = {r.c1RGB[0] - size, r.c1RGB[1] - size, r.c1RGB[2] - size};
		int [] okColor = null;
		if(r instanceof MirrorRect)
			okColor = ((MirrorRect)r).controlReflection(u, spheres, nC); // ak je stvorec zrkadlo, kontrola farby objektu ak do nejakeho narazi
		if(okColor != null)
			nC = okColor;

		if(L1.controlShadow(r, spheres)) { // ak medzi bodom a svetlom nieco je, tak jednoducho farby podelime dvomi
			nC[0] >>= 1; // >>1 je vraj rychlejsie nez /2
			nC[1] >>= 1;
			nC[2] >>= 1;
		}

		im[ray.Y2][ray.X2].setColor(nC);
		im[ray.Y2][ray.X2].o = o;

		g.setColor(new Color(im[ray.Y2][ray.X2].getR(), im[ray.Y2][ray.X2].getG(), im[ray.Y2][ray.X2].getB()));
		g.drawLine(ray.X2, ray.Y2, ray.X2, ray.Y2);
	}
	/**
	 * test luca ray vzhladom na gulu r
	 * @param ray
	 * @param r
	 */
	public void fireRayToSphere(Line ray, Sphere r) {
		double [] t = r.getIntersectionPoint(ray); //vypocet bodu stretnutia ak existuje
		if(t == null) return;
		ray = ray.pointTo(t);
		if(im[ray.Y2][ray.X2] == null) im[ray.Y2][ray.X2] = new PixelMap(ray.X2, ray.Y2, ray.getSize());
		else if(im[ray.Y2][ray.X2].depth < ray.getSize()) return;
		im[ray.Y2][ray.X2].depth = ray.getSize();
		im[ray.Y2][ray.X2].setColor(r.c1RGB[0], r.c1RGB[1], r.c1RGB[2]);
		im[ray.Y2][ray.X2].o = r;
		g.setColor(new Color(r.c1RGB[0], r.c1RGB[1], r.c1RGB[2]));
		g.drawLine(ray.X2, ray.Y2, ray.X2, ray.Y2);
	}

	public static void main(String[] args) {
		mf = new MainFrame(new rt(), 850, 590);
	}

	/**
	 * aplikacie zmien pri posune nejakeho scrollbaru nastaveni objektu
	 * (funkcie snad pochopitelne z nazvov metod)
	 */
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		int minY = 0;
		int minX = 0;
		int maxY = 0;
		int maxX = 0;
		if(selObj instanceof Cube) {
			minY = ((Cube)selObj).minY;
			minX = ((Cube)selObj).minX;
			maxY = ((Cube)selObj).maxY;
			maxX = ((Cube)selObj).maxX;
		}
		if(selObj instanceof Sphere) {
			minY = ((Sphere)selObj).minY;
			minX = ((Sphere)selObj).minX;
			maxY = ((Sphere)selObj).maxY;
			maxX = ((Sphere)selObj).maxX;
		}

		if(e.getSource().equals(cubeScrolls[0][0])) ((Cube)selObj).moveToX((double)cubeScrolls[0][0].getValue()/5);
		if(e.getSource().equals(cubeScrolls[0][1])) ((Cube)selObj).moveToY((double)cubeScrolls[0][1].getValue()/5);
		if(e.getSource().equals(cubeScrolls[0][2])) ((Cube)selObj).moveToZ((double)cubeScrolls[0][2].getValue()/5);

		if(e.getSource().equals(cubeScrolls[1][0])) ((Cube)selObj).resizeByX((double)cubeScrolls[1][0].getValue()/2);
		if(e.getSource().equals(cubeScrolls[1][1])) ((Cube)selObj).resizeByY((double)cubeScrolls[1][1].getValue()/2);
		if(e.getSource().equals(cubeScrolls[1][2])) ((Cube)selObj).resizeByZ((double)cubeScrolls[1][2].getValue()/2);

		if(e.getSource().equals(cubeScrolls[2][0])) ((Cube)selObj).rotateByX((double)cubeScrolls[2][0].getValue());
		if(e.getSource().equals(cubeScrolls[2][1])) ((Cube)selObj).rotateByY((double)cubeScrolls[2][1].getValue());
		if(e.getSource().equals(cubeScrolls[2][2])) ((Cube)selObj).rotateByZ((double)cubeScrolls[2][2].getValue());

		if(e.getSource().equals(cubeScrolls[3][0])) ((Cube)selObj).changeColorR(cubeScrolls[3][0].getValue());
		if(e.getSource().equals(cubeScrolls[3][1])) ((Cube)selObj).changeColorG(cubeScrolls[3][1].getValue());
		if(e.getSource().equals(cubeScrolls[3][2])) ((Cube)selObj).changeColorB(cubeScrolls[3][2].getValue());

		if(e.getSource().equals(sphereScrolls[0][0])) ((Sphere)selObj).moveToX((double)sphereScrolls[0][0].getValue()/5);
		if(e.getSource().equals(sphereScrolls[0][1])) ((Sphere)selObj).moveToY((double)sphereScrolls[0][1].getValue()/5);
		if(e.getSource().equals(sphereScrolls[0][2])) ((Sphere)selObj).moveToZ((double)sphereScrolls[0][2].getValue()/5);

		if(e.getSource().equals(sphereScrolls[1][0])) ((Sphere)selObj).setRadius((double)sphereScrolls[1][0].getValue()/2);

		if(e.getSource().equals(sphereScrolls[2][0])) ((Sphere)selObj).c1RGB[0] = sphereScrolls[2][0].getValue();
		if(e.getSource().equals(sphereScrolls[2][1])) ((Sphere)selObj).c1RGB[1] = sphereScrolls[2][1].getValue();
		if(e.getSource().equals(sphereScrolls[2][2])) ((Sphere)selObj).c1RGB[2] = sphereScrolls[2][1].getValue();

		if(selObj instanceof Cube) {
			((Cube)selObj).setVisibleSides(cam);
			setSides();
			if(minY != ((Cube)selObj).minY || maxY != ((Cube)selObj).maxY && minX != ((Cube)selObj).minX || maxX != ((Cube)selObj).maxX)
				drawImagePart(minY, maxY, minX, maxX); // prekreslenie casti obrazku ak sme posuvali obrysove ciary
			drawCube((Cube)selObj, new Color(255, 0, 0));
		}
		if(selObj instanceof Sphere) {
			if(minY != ((Sphere)selObj).minY || maxY != ((Sphere)selObj).maxY && minX != ((Sphere)selObj).minX || maxX != ((Sphere)selObj).maxX)
				drawImagePart(minY, maxY, minX, maxX); // prekreslenie casti obrazku ak sme posuvali obrysovu kruznicu
			drawSphere((Sphere)selObj, new Color(255, 0, 0));
		}
	}
	public void mouseEntered  (MouseEvent e) { }
	public void mouseExited   (MouseEvent e) { }
	public void mouseClicked  (MouseEvent e) {
		removeForm();

		if(e.getButton() == 1) {
			if(obj != null) {
				if(obj instanceof Cube) { //vykreslenie obrysovej kruznice  po kliknuti
					if(selObj instanceof Cube)
	   					drawImagePart(((Cube)selObj).minY, ((Cube)selObj).maxY, ((Cube)selObj).minX, ((Cube)selObj).maxX);
					if(selObj instanceof Sphere)
	   					drawImagePart(((Sphere)selObj).minY, ((Sphere)selObj).maxY, ((Sphere)selObj).minX, ((Sphere)selObj).maxX);
	   				drawCube((Cube)obj, new Color(255, 0, 0));
				}
				if(obj instanceof Sphere) { //vytvorenie obrysovych ciar kocky po kliknuti
					if(selObj instanceof Cube)
	   					drawImagePart(((Cube)selObj).minY, ((Cube)selObj).maxY, ((Cube)selObj).minX, ((Cube)selObj).maxX);
					if(selObj instanceof Sphere)
	   					drawImagePart(((Sphere)selObj).minY, ((Sphere)selObj).maxY, ((Sphere)selObj).minX, ((Sphere)selObj).maxX);
	   				drawSphere((Sphere)obj, new Color(255, 0, 0));
				}
				
				selObj = obj;
	   			g.setColor(new Color(0, 0, 0));
	   			g.fillRect(580, 30, 270, 550);
	   			g.setColor(new Color(255, 255, 255));
				String text = selObj+" selected";
	   			g.setFont(new Font("Courier New", 0, 15));
	   			g.drawBytes(text.getBytes(), 0, text.length(), 600, 45);
	   			g.setFont(new Font("Courier New", 0, 11));
	   			// formulare...
	   			if(selObj instanceof Cube) {

	   				makeBigLabel(0, "Position:", 58);
	   				makeCubeBox(0, 0, 70,  "X:", (int)Math.round(((Cube)selObj).v[0].X*5), 180);
	   				makeCubeBox(0, 1, 90,  "Y:", (int)Math.round(((Cube)selObj).v[0].Y*5), 180);
	   				makeCubeBox(0, 2, 110, "Z:", (int)Math.round(((Cube)selObj).v[0].Z*5), 180);

	   				makeBigLabel(1, "Size:", 133);
	   				makeCubeBox(1, 0, 145, "W:", (int)Math.round(((Cube)selObj).v[0].getSize()*2), 40);
	   				makeCubeBox(1, 1, 165, "H:", (int)Math.round(((Cube)selObj).v[1].getSize()*2), 40);
	   				makeCubeBox(1, 2, 185, "W:", (int)Math.round(((Cube)selObj).v[2].getSize()*2), 40);

	   				makeBigLabel(2, "Angle(nefici, ale vyzera to pekne ;-):", 208);
	  				makeCubeBox(2, 0, 220, "X:", (int)((Cube)selObj).angleX, 90);
	   				makeCubeBox(2, 1, 240, "Y:", (int)((Cube)selObj).angleY, 90);
	   				makeCubeBox(2, 2, 260, "Z:", (int)((Cube)selObj).angleZ, 90);

	   				makeBigLabel(3, "Color:", 283);
	   				makeCubeBox(3, 0, 295, "R:", ((Cube)selObj).cR, 255);
	   				makeCubeBox(3, 1, 315, "G:", ((Cube)selObj).cG, 255);
	   				makeCubeBox(3, 2, 335, "B:", ((Cube)selObj).cB, 255);
	   				removeIt = new Button("Remove Object");
	   				removeIt.setBounds(590, 355, 100, 20);
	   				removeIt.addActionListener(this);
	   				add(removeIt);
	   			}
	   			if(selObj instanceof Sphere) {

	   				makeBigLabel(0, "Position:", 58);
	   				makeSphereBox(0, 0, 70,  "X:", (int)Math.round(((Sphere)selObj).middle.X*5), 180);
	   				makeSphereBox(0, 1, 90,  "Y:", (int)Math.round(((Sphere)selObj).middle.Y*5), 180);
	   				makeSphereBox(0, 2, 110, "Z:", (int)Math.round(((Sphere)selObj).middle.Z*5), 180);

	   				makeBigLabel(1, "Radius:", 133);
	   				makeSphereBox(1, 1, 145, "R:", (int)Math.round(((Sphere)selObj).radius*2), 20);

	   				makeBigLabel(2, "Color:", 168);
	   				makeSphereBox(2, 0, 180, "R:", ((Sphere)selObj).c1RGB[0], 255);
	   				makeSphereBox(2, 1, 200, "G:", ((Sphere)selObj).c1RGB[1], 255);
	   				makeSphereBox(2, 2, 220, "B:", ((Sphere)selObj).c1RGB[2], 255);
	   				removeIt = new Button("Remove Object");
	   				removeIt.setBounds(590, 240, 100, 20);
	   				removeIt.addActionListener(this);
	   				add(removeIt);
	   			}
			}
		}
	}
	public void mouseReleased (MouseEvent e) { }
	public void mousePressed  (MouseEvent e) { }
	/**
	 * okntrola, ci som mysou nad nejakym znamym objektom
	 */
	public void mouseMoved    (MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		this.g = getGraphics(); 
		obj = null;
		if(y < (int)Line.wLength && x < (int)Line.wLength && im != null && im[y][x] != null && im[y][x].o instanceof Cube) {
			obj = im[y][x].o;
   			g.setColor(new Color(0, 0, 0));
   			g.fillRect(580, 0, 220, 30);
   			g.setColor(new Color(255, 255, 255));
   			g.setFont(new Font("Courier New", 0, 15));
   			g.drawBytes(im[y][x].o.toString().getBytes(), 0, im[y][x].o.toString().length(), 600, 20);
		}
		if(y < (int)Line.wLength && x < (int)Line.wLength && im != null && im[y][x] != null && im[y][x].o instanceof Sphere) {
			obj = im[y][x].o;
   			g.setColor(new Color(0, 0, 0));
   			g.fillRect(580, 0, 220, 30);
   			g.setColor(new Color(255, 255, 255));
   			g.setFont(new Font("Courier New", 0, 15));
   			g.drawBytes(im[y][x].o.toString().getBytes(), 0, im[y][x].o.toString().length(), 600, 20);
		}
	}
	public void mouseDragged  (MouseEvent e) { }
	/**
	 * klik...
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(traceIt)) {
			removeForm();
			rtrace();
		}
		if(e.getSource().equals(addCube)) {
			addCube();
			removeForm();
			rtrace();
		}
		if(e.getSource().equals(addSphere)) {
			addSphere();
			removeForm();
			rtrace();
		}
		if(e.getSource().equals(removeIt)) {
			removeObject();
			removeForm();
			rtrace();
		}
	}
}
/**
 * trieda pixelmap kde si ukladam info o najdenom bode vo svete
 * @author Sharkey
 *
 */
class PixelMap {
	public int Y;
	public int X;
	public double depth;
	public int [] color = new int[3];
	public Object o;
	public PixelMap(int x, int y, double depth) {
		this.X = x;
		this.Y = y;
		this.depth = depth;
	}
	public int getR() {
		int c = (int)(color[0]);
		return (c > 255) ? 255 : ((c < 0) ? 0 : c);
	}
	public int getG() {
		int c = (int)(color[1]);
		return (c > 255) ? 255 : ((c < 0) ? 0 : c);
	}
	public int getB() {
		int c = (int)(color[2]);
		return (c > 255) ? 255 : ((c < 0) ? 0 : c);
	}
	public void setColor(int [] color) {
		this.color = color;
	}
	public void setColor(int r, int g, int b) {
		this.color[0] = r;
		this.color[1] = g;
		this.color[2] = b;
	}
}
