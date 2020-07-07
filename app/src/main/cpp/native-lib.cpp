#include <jni.h>
#include <string>

/**
 * @Auther: JerryZhu
 * @datetime: 2020/7/7
 */

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jerryzhu_example_breakpaddemo_MainActivity_doCrash(JNIEnv *env, jobject thiz) {
    volatile int* a = reinterpret_cast<volatile int*>(NULL);
    *a = 1;
}