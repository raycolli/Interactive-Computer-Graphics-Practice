
// ppmtex.c
#include <stdio.h>
#include <stdlib.h>
#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GLUT/glut.h>
#endif
int n;
int m;
GLuint *image;
void display()
{
glClear(GL_COLOR_BUFFER_BIT);
glBegin(GL_QUADS);
glTexCoord2f(0.0,0.0);
glVertex2i(0,0);
glTexCoord2f(0.0,1.0);
glVertex2i(0,m-1);
glTexCoord2f(1.0,1.0);
glVertex2i(n-1,m-1);
glTexCoord2f(1.0,0.0);
glVertex2i(n-1,0);
glEnd();
glFlush();
}
void myreshape(int h, int w)
{
glMatrixMode(GL_PROJECTION);
glLoadIdentity();
gluOrtho2D(0.0, (GLfloat) n, 0.0, (GLfloat) m);
glMatrixMode(GL_MODELVIEW);
glLoadIdentity();
glViewport(0,0,h,w);
}
int main(int argc, char **argv)
{
FILE *fd;
int k, nm;
char c;
int i;
char b[100];
float s;
int red, green, blue;
printf("enter file name\n");
scanf("%s", b);
fd = fopen(b, "r");
fscanf(fd,"%[^\n] ",b);
if(b[0]!='P'|| b[1] != '3')
{
printf("%s is not a PPM file!\n", b);
exit(0);
}
printf("%s is a PPM file\n",b);
fscanf(fd, "%c",&c);
while(c == '#')
{
fscanf(fd, "%[^\n] ", b);
printf("%s\n",b);
fscanf(fd, "%c",&c);
}
ungetc(c,fd);
fscanf(fd, "%d %d %d", &n, &m, &k);
printf("%d rows %d columns max value= %d\n",n,m,k);
nm = n*m;
image=malloc(3*sizeof(GLuint)*nm);
s=255./k;
for(i=0;i<nm;i++)
{
fscanf(fd,"%d %d %d",&red, &green, &blue );
image[3*nm-3*i-3]=red;
image[3*nm-3*i-2]=green;
image[3*nm-3*i-1]=blue;
}
printf("read image\n");
glutInit(&argc, argv);
glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB );
glutInitWindowSize(n, m);
glutInitWindowPosition(0,0);
glutCreateWindow("ppm textured image");
glutReshapeFunc(myreshape);
glutDisplayFunc(display);
glPixelTransferf(GL_RED_SCALE, s);
glPixelTransferf(GL_GREEN_SCALE, s);
glPixelTransferf(GL_BLUE_SCALE, s);
glPixelStorei(GL_UNPACK_SWAP_BYTES,GL_TRUE);
glPixelStorei(GL_UNPACK_ALIGNMENT,1);
glEnable(GL_TEXTURE_2D);
glTexImage2D(GL_TEXTURE_2D,0,3,n,m,0,GL_RGB,GL_UNSIGNED_INT, image);
glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP);
glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP);
glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST);
glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST);
glClearColor(0.0, 0.0, 0.0, 1.0);
glColor3f(1.0,1.0,1.0);
glutMainLoop();
}

