#include <stdio.h>
#include <jni.h>
#include <android/log.h>

#include "client/linux/handler/exception_handler.h"
#include "client/linux/handler/minidump_descriptor.h"


/**
 * @Auther: JerryZhu
 * @datetime: 2020/7/7
 */

#define LOG_TAG "native_crash"

#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


bool DumpCallback(const google_breakpad::MinidumpDescriptor &descriptor,
                  void *context,
                  bool succeeded) {
    ALOGE("crashDump path: %s\n", descriptor.path());
    return succeeded;
}

extern "C" JNIEXPORT void JNICALL
Java_com_jerryzhu_lib_breakpad_Breakpad_nativeBreakpadInit(JNIEnv *env, jclass clazz,jstring path) {

    const char *_path = env->GetStringUTFChars(path, 0);

    google_breakpad::MinidumpDescriptor descriptor(_path);
    static google_breakpad::ExceptionHandler eh(descriptor, NULL, DumpCallback, NULL, true, -1);

    env->ReleaseStringUTFChars(path, _path);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    return JNI_VERSION_1_6;
}