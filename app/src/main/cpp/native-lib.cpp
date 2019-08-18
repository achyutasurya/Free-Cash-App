//
// Created by ravi on 2/13/18.
//
#include <jni.h>
#include <string>
#include <android/log.h>

#define TAG "AndroidNDKDemos"


extern "C" {

// Define keys/Values for all the string constants
const char *mStringConstantKeys[] = {"appId", "clientKey", "server"};

const char *mStringConstantValues[] = {"APPID", "CLIENTKEY",
                                       "SERVERIP"};

// Define keys/values for all the integer constants
const char *mIntegerConstantKeys[] = {"db_version", "max_reversal_attempt", "phone_number",
                                      "nrc_length"};
const int mIntegerConstantValues[] = {1, 3, 11, 6};


// Method  to return constant integer
jint
Java_com_dream_earntwo_StarterApplication_getMyInts(JNIEnv *env,
                                                         jobject instance,
                                                         jstring iKeyName_) {
    // Check for the key
    const char *keyName = env->GetStringUTFChars(iKeyName_, 0);

    // Value to be returned
    int value = -1; // Default

    for (int index = 0; index < 4; index++) {
        int compResult = strcmp(keyName, mIntegerConstantKeys[index]);
        if (compResult == 0) {
            value = mIntegerConstantValues[index];
            break;
        }
    }

    __android_log_print(ANDROID_LOG_INFO, TAG, "String constant resolved for key %s is  %d",
                        keyName, value);

    env->ReleaseStringUTFChars(iKeyName_, keyName);

    return value;
}


// Method to return constant string based on the key  Java_com_drunkendogs_freemoneyonline_StarterApplication_getMyStrings
jstring
Java_com_dream_earntwo_StarterApplication_getMyStrings(JNIEnv *env,
                                                            jobject instance,
                                                            jstring iKeyName_) {
    const char *keyName = env->GetStringUTFChars(iKeyName_, 0);

    // Value to be returned to the caller method
    const char *value = "Default"; // Default

    for (int index = 0; index < 3; index++) {
        int compResult = strcmp(keyName, mStringConstantKeys[index]);
        if (compResult == 0) {
            value = mStringConstantValues[index];
            break;
        }
    }

    env->ReleaseStringUTFChars(iKeyName_, keyName);

    return env->NewStringUTF(value);
}


}
