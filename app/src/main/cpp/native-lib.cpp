#include <jni.h>
#include <string>
#include "breakpad/src/client/linux/handler/exception_handler.h"
#include "breakpad/src/client/linux/handler/minidump_descriptor.h"

/**
 * 引起 crash
 */
void Crash() {
    volatile int *a = (int *) (NULL);
    *a = 1;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_elijah_nativecrashdemo_MainActivity_nativeCrash(JNIEnv *env, jobject thiz) {
    Crash();
}

//回调函数
bool DumpCallback(const google_breakpad::MinidumpDescriptor& descriptor,
                  void* context,
                  bool succeeded) {
    printf("Dump path: %s\n", descriptor.path());
    return false;
}

//breakpad 初始化
extern "C"
JNIEXPORT void JNICALL
Java_com_elijah_nativecrashdemo_MainActivity_initNative(JNIEnv *env, jclass clazz, jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);
    google_breakpad::MinidumpDescriptor descriptor(path);
    static google_breakpad::ExceptionHandler eh(descriptor, NULL, DumpCallback,
                                                NULL, true, -1);
    env->ReleaseStringUTFChars(path_, path);
}
