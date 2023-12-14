
// Your Last, First Name
// Lecture 2 Fall 2023
/* house.c - open a window and clear the background.
* Set up callbacks to handle keyboard input.
* Model house from objects.
*
* F1 key - print help information
* SPACE key - generates a random background color
* <s> key - toggle smooth/flat shading
* Escape Key - exit program
*/
#include <stdlib.h>
#ifdef __APPLE_
#include <GLUT/glut.h>
#else
#include <GLUT/glut.h>
#endif
#include <stdio.h>
/* Global Variables */
static char *progname = "house";
/* Global Definitions */
#define KEY_ESC 27 /* ascii value for the escape key */
void printHelp( char *progname )
{
fprintf(stdout,
"\n%s - model some objects\n\n"
"F1 key - print help information\n"
"SPACE Key - generates a random background color\n"
"<s> key - toggle smooth/flat shading\n"
"Escape Key - exit the program\n\n",
progname);
}
GLvoid myReshape( int w, int h )
{
/* set clear color to blue */
glClearColor( 0.0f, 0.0f, 1.0f, 1.0f );
glOrtho( -2.0f, 2.0f, -2.0f, 2.0f, -1.0f, 1.0f );
}
GLvoid keyboard( GLubyte key, GLint x, GLint y )
{
static GLboolean flat = GL_FALSE;
switch (key) {
case ' ': /* SPACE key */
/* generate a random background color */
glClearColor( rand(), rand(), rand(), 1.0f );
glutPostRedisplay();
break;
case 's': /* s key */
/* toggle between smooth and flat shading */
flat = !flat;
if (flat)
glShadeModel( GL_FLAT );
else
glShadeModel( GL_SMOOTH );
glutPostRedisplay();
break;
case KEY_ESC: /* Exit when the Escape key is pressed */
exit(0);
}
}
GLvoid specialkeys( GLint key, GLint x, GLint y )
{
switch (key) {
case GLUT_KEY_F1: /* Function key #1 */
/* print help information */
printHelp( progname );
break;
}
}
GLvoid drawScene( GLvoid )
{
static GLfloat red[] = { 1.0f, 0.0f, 0.0f };
static GLfloat yellow[] = { 1.0f, 1.0f, 0.0f };
static GLfloat blue[] = { 0.0f, 0.0f, 1.0f };
static GLfloat green[] = { 0.0f, 1.0f, 0.0f };
static GLfloat darkgreen[] = { 0.0f, 0.25f, 0.0f };
/* left window */
static GLfloat v0[] = { -0.4f, 0.8f };
static GLfloat v1[] = { -0.4f, 0.4f };
static GLfloat v2[] = { -0.3f, 0.8f };
static GLfloat v3[] = { -0.3f, 0.4f };
static GLfloat v4[] = { -0.2f, 0.8f };
static GLfloat v5[] = { -0.2f, 0.4f };
/* right window */
static GLfloat v6[] = { 0.2f, 0.8f };
static GLfloat v7[] = { 0.2f, 0.4f };
static GLfloat v8[] = { 0.3f, 0.8f };
static GLfloat v9[] = { 0.3f, 0.4f };
static GLfloat v10[] = { 0.4f, 0.8f };
static GLfloat v11[] = { 0.4f, 0.4f };
glClear( GL_COLOR_BUFFER_BIT );
/* draw the ground in different shades of green */
glBegin( GL_POLYGON );
glColor3fv( green );
glVertex2f( -2.0f, -2.0f );
glVertex2f( 2.0f, -2.0f );
glColor3fv( darkgreen );
glVertex2f( 2.0f, 0.0f );
glVertex2f( -2.0f, 0.0f );
glEnd();
/* draw the house */
glColor3f( 1.0f, 1.0f, 1.0f ); /* white */
glRectf( -0.5f, -0.5f, 0.5f, 1.0f );
/* draw the door */
glColor3f( 0.5f, 0.2f, 0.1f ); /* brown */
glRectf( -0.2f, -0.5f, 0.2f, 0.2f );
/* draw the roof */
glBegin( GL_TRIANGLES );
glColor3f( 0.0f, 0.0f, 0.0f ); /* black */
glVertex2f( -0.5f, 1.0f );
glVertex2f( 0.5f, 1.0f );
glVertex2f( 0.0f, 1.5f );
glEnd();
/* draw 2 quadrilateral strip to make a window */
glBegin( GL_QUAD_STRIP );
glColor3f( 1.0f, 0.0f, 0.0f );
glVertex2fv (v0);
glColor3f( 0.9f, 0.0f, 1.0f );
glVertex2fv (v1);
glColor3f( 0.8f, 0.1f, 0.0f );
glVertex2fv (v2);
glColor3f( 0.7f, 0.2f, 1.0f );
glVertex2fv (v3);
glColor3f( 0.6f, 0.3f, 0.0f );
glVertex2fv (v4);
glColor3f( 0.5f, 0.4f, 1.0f );
glVertex2fv (v5);
glColor3f( 0.4f, 0.5f, 0.0f );
glEnd();
/* draw 2 quadrilateral strip to make a window */
glBegin( GL_QUAD_STRIP );
glVertex2fv (v6);
glColor3f( 0.3f, 0.6f, 1.0f );
glVertex2fv (v7);
glColor3f( 0.2f, 0.7f, 0.0f );
glVertex2fv (v8);
glColor3f( 0.1f, 0.8f, 1.0f );
glVertex2fv (v9);
glColor3f( 0.0f, 0.9f, 0.0f );
glVertex2fv (v10);
glColor3f( 0.0f, 1.0f, 1.0f );
glVertex2fv (v11);
glEnd();
/* Draw a triangle fan for the sun; make
* the center white, and the edges yellow
*/
glBegin( GL_TRIANGLE_FAN );
glColor3f( 1.0f, 1.0f, 1.0f );
glVertex2f( 1.5f, 1.5f );
glColor3fv( yellow );
glVertex2f( 1.5f, 1.3f );
glVertex2f( 1.7f, 1.4f );
glVertex2f( 1.7f, 1.6f );
glVertex2f( 1.5f, 1.7f );
glVertex2f( 1.3f, 1.6f );
glVertex2f( 1.3f, 1.4f );
glVertex2f( 1.5f, 1.3f );
glEnd();
glFlush();
}
int main( int argc, char *argv[] )
{
GLsizei width, height;
glutInit( &argc, argv );
/* create a window that is 1/4 the size of the screen,
* and position it in the middle of the screen.
*/
width = glutGet( GLUT_SCREEN_WIDTH );
height = glutGet( GLUT_SCREEN_HEIGHT );
glutInitWindowPosition( width / 4, height / 4 );
glutInitWindowSize( width / 2, height / 2 );
glutInitDisplayMode( GLUT_RGBA );
glutCreateWindow(progname );
glutReshapeFunc(myReshape);
glutKeyboardFunc( keyboard );
glutSpecialFunc( specialkeys );
glutDisplayFunc( drawScene );
printHelp( progname );
glutMainLoop();
}

