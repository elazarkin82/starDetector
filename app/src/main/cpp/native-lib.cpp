#include <jni.h>
#include <android/bitmap.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

typedef unsigned char uchar;

typedef struct
{
    uchar r, g, b, a;
}ARGB;

typedef struct
{
    int x, y;
}Point;

bool checkStar(uchar *ybuffer, int x, int y, int radius, jint w, int thresh, Point *p);

extern "C" JNIEXPORT void JNICALL
Java_tracker_star_kcg_myapplication_MainActivity_findStarsJNI(JNIEnv *env, jobject instance, jbyteArray ybuffer_, jint  w, jint  h, jbyteArray textOut_, jobject _debug)
{
    int RADIUS = 5;
    uchar *ybuffer = (uchar *) env->GetByteArrayElements(ybuffer_, NULL);
    char *textOut = (char *) env->GetByteArrayElements(textOut_, NULL);
    Point p;
    ARGB *debug = NULL;

    textOut[0] = 0;

    AndroidBitmap_lockPixels(env, _debug, (void **) &debug);

    memset(debug, 0, w*h*sizeof(ARGB));

    // TODO
    for(int y = RADIUS; y < h-RADIUS; y += RADIUS*2)
    {
        for (int x = RADIUS; x < w-RADIUS; x += RADIUS*2)
        {
            if(checkStar(ybuffer, x, y, RADIUS, w, 100, &p))
            {
                ARGB *pixel = &debug[p.y*w + p.x];

                pixel->a = 255;
                pixel->r = 255;
                pixel->g = 0;
                pixel->b = 0;

            }
        }
    }

    AndroidBitmap_unlockPixels(env, _debug);

    env->ReleaseByteArrayElements(ybuffer_, (jbyte *) ybuffer, 0);
    env->ReleaseByteArrayElements(textOut_, (jbyte *) textOut, 0);
}

int int_compire(const void * a, const void * b)
{
    return ( *(int*)a - *(int*)b );
}

bool checkStar(uchar *ybuffer, int xc, int yc, int radius, int w, int tresh, Point *p)
{
    int *buffer = (int *)malloc(sizeof(int)*radius*radius + 128);
    int max = 0;
    int index = 0;
    bool ret = false;
    int percent = 80;

    for(int y = yc - radius; y < yc + radius; ++y)
    {
        for (int x = xc - radius; x < xc + radius; ++x)
        {
            buffer[index] = ybuffer[y*w + x];

            if(buffer[index] > max)
            {
                max = buffer[index];
                p->x = x;
                p->y = y;
            }
            index++;
        }
    }

    qsort(buffer, index, sizeof(int), int_compire);

    if(buffer[index-1] - buffer[radius*radius*percent/100] > tresh) ret = true;

    free(buffer);
    return ret;
}
