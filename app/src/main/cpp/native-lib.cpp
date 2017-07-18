#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <string.h>

#define CLIP(X) ( (X) > 255 ? 255 : (X) < 0 ? 0 : X)
#define RGB2Y(R, G, B) CLIP(( (  66 * (R) + 129 * (G) +  25 * (B) + 128) >> 8) +  16)

#ifdef __cplusplus
extern "C" {
#endif

//#define LOG(TAG,...) __android_log_print(ANDROID_LOG_DEBUG  , TAG,__VA_ARGS__)
#define LOG(...) __android_log_print(ANDROID_LOG_DEBUG  , "elazarkinJNI",__VA_ARGS__)

typedef unsigned char uchar;

typedef struct
{
    uchar r, g, b, a;
}RGBA;

typedef struct
{
    uchar r, g, b, a;
}ARGB;

typedef struct
{
    int x, y;
}Point;

int findMaxArea(int *buffer, int size, int area_size);

int findMaxArea(int *buffer, int size, int area_size)
{
    int sum = 0;
    int max = 0;
    int ret = 0;

    for(int i = 0; i < size - area_size; i++)
    {
        if(i > 0)
        {
            sum -= buffer[i-1];
            sum += buffer[i+area_size-1];

            if(sum > max)
            {
                ret = i;
                sum = max;
            }
        }
        else
        {
            for(int j = 0; j < area_size; j++)
            {
                sum += buffer[j];
                max = sum;
            }
        }
    }
    return ret;
}

typedef struct
{
    int r;//radius
    int p;//percents
    int t;//treshhold
}AlgorithmProps;

bool checkStar(uchar *ybuffer, int xc, int yc, int w, AlgorithmProps *props, Point *p);

JNIEXPORT void JNICALL
Java_tracker_star_kcg_startracker_MainActivity_colorBitmapToYuyvBuffer(JNIEnv *env, jobject instance, jobject bit_, jint w, jint h, jbyteArray buffer_)
{
    uchar *buffer = (uchar *) env->GetByteArrayElements(buffer_, NULL);
    ARGB *bit = NULL;

    AndroidBitmap_lockPixels(env, bit_, (void **) &bit);

    LOG("Java_tracker_star_kcg_startracker_MainActivity_colorBitmapToYuyvBuffer");

    for(int i = 0; i < w*h; i++)
    {
        buffer[i] = RGB2Y(bit[i].r, bit[i].g, bit[i].b);
    }

    LOG("Java_tracker_star_kcg_startracker_MainActivity_colorBitmapToYuyvBuffer end");

    AndroidBitmap_unlockPixels(env, bit_);
    env->ReleaseByteArrayElements(buffer_, (jbyte *) buffer, 0);
}

JNIEXPORT int JNICALL
Java_tracker_star_kcg_myapplication_MainActivity_findStarsJNI(JNIEnv *env, jobject instance, jbyteArray ybuffer_, jint  w, jint  h, jbyteArray textOut_, jobject _debug)
{
    uchar *ybuffer = (uchar *) env->GetByteArrayElements(ybuffer_, NULL);
    char *textOut = (char *) env->GetByteArrayElements(textOut_, NULL);
    Point p;
    ARGB *debug = NULL;
    RGBA red = {255, 0, 0, 255};
    int text_index = 0;

    AlgorithmProps props;// = {5, 80, 150};

    props.r = w/100;
    props.p = 80;
    props.t = 50;

    LOG("Java_tracker_star_kcg_startracker_MainActivity_findStarsJNI");
    textOut[0] = 0;

    AndroidBitmap_lockPixels(env, _debug, (void **) &debug);

//    memset(debug, 0, w*h*sizeof(ARGB));

    for(int i = 0; i < w*h; i++)
    {
//        memset(&debug[i], ybuffer[i], sizeof(ARGB));
        debug[i].r = debug[i].g = debug[i].b = ybuffer[i];
        debug[i].a = 255;
    }

    for(int y = props.r; y < h-props.r; y += props.r)
    {
        for (int x = props.r; x < w-props.r; x += props.r)
        {
            if(checkStar(ybuffer, x, y, w, &props, &p))
            {
                text_index += sprintf(&textOut[text_index], "%d %d\n", p.x, p.y);
            }
        }
    }

    textOut[text_index] = 0;

    AndroidBitmap_unlockPixels(env, _debug);

    env->ReleaseByteArrayElements(ybuffer_, (jbyte *) ybuffer, 0);
    env->ReleaseByteArrayElements(textOut_, (jbyte *) textOut, 0);

    return text_index;
}

int int_compire(const void * a, const void * b)
{
    return ( *(int*)a - *(int*)b );
}

bool checkStar(uchar *ybuffer, int xc, int yc, int w, AlgorithmProps *props, Point *p)
{
    int *buffer = (int *)malloc(sizeof(int)*props->r*props->r*4 + 128);
    int max = 0;
    int index = 0;
    bool ret = false;
    int percent = 90;

    for(int y = yc - props->r; y < yc + props->r; ++y)
    {
        for (int x = xc - props->r; x < xc + props->r; ++x)
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
//
    if(buffer[index-1] - buffer[index*percent/100-1] > props->t) ret = true;

    free(buffer);
    return ret;
}

#ifdef __cplusplus
}
#endif