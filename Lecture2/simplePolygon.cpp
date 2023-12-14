
// Your Last, First Name
// Lecture 2 Fall 2023
/* simplepolygon.c This program draws a white rectangle on a black background. */
#include <GLUT/glut.h> /* glut.h includes gl.h and glu.h*/
void display(void)
{
glClear(GL_COLOR_BUFFER_BIT);
/* default viewing volume is a cube of side 2 centered at the origin*/
/* thus, this square is centered at the origin*/
glBegin(GL_POLYGON);
glVertex2f(-0.5, -0.5);
glVertex2f(-0.5, 0.5);
glVertex2f( 0.5, 0.5);
glVertex2f( 0.5, -0.5);
glEnd();
glFlush();
}
int main(int argc, char** argv)
{
glutInit(&argc,argv);
glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB);
glutInitWindowSize(500,500);
glutInitWindowPosition(0,0);
glutCreateWindow("simplePolygon");
glutDisplayFunc(display);
glutMainLoop();
}

