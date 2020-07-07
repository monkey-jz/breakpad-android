#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jerryzhu_example_breakpaddemo_MainActivity_doCrash(JNIEnv *env, jobject thiz) {
    volatile int* a = reinterpret_cast<volatile int*>(NULL);
    *a = 1;
}