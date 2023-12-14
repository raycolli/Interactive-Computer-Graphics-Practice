
// Last Name, First Name
// Lecture1
// Fall 2023
/* simple.c This program draws a white rectangle on a black background. */
#include <GLUT/glut.h> /* glut.h includes gl.h and glu.h*/
void display(void)
{
glClear(GL_COLOR_BUFFER_BIT);
/* default viewing volume is a cube of side 2 centered at the origin*/
/* thus, this square is centered at the origin*/
glBegin(GL_TRIANGLES);
glVertex2f(0.0, 0.0);
glVertex2f(1.0, 1.0);
glVertex2f(1.0, 0.0);
glEnd();
glFlush();
}
int main(int argc, char** argv)
{
glutInit(&argc,argv);
glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB);
glutInitWindowSize(500,500);
glutInitWindowPosition(0,0);
glutCreateWindow("simple");
glutDisplayFunc(display);
glutMainLoop();
}
