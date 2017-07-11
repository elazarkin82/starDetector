#include <jni.h>
#include <android/bitmap.h>
#include <string.h>

typedef unsigned char uchar;

typedef struct
{
    uchar r, g, b, a;
}ARGB;

extern "C" JNIEXPORT void JNICALL
Java_tracker_star_kcg_myapplication_MainActivity_findStarsJNI(JNIEnv *env, jobject instance, jbyteArray ybuffer_, jint  w, jint  h, jbyteArray textOut_, jobject _debug)
{
    uchar *ybuffer = (uchar *)env->GetByteArrayElements(ybuffer_, NULL);
    jbyte *textOut = env->GetByteArrayElements(textOut_, NULL);

    ARGB *debug = NULL;

    AndroidBitmap_lockPixels(env, _debug, (void **) &debug);

    // TODO
    for(int i = 0; i < w*h/2; i++)
    {
        memset(&debug[i], ybuffer[i], sizeof(ARGB));
        debug[i].a = 255;
    }

    AndroidBitmap_unlockPixels(env, _debug);

    env->ReleaseByteArrayElements(ybuffer_, (jbyte *) ybuffer, 0);
    env->ReleaseByteArrayElements(textOut_, textOut, 0);
}
