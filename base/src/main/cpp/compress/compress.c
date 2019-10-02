//
// Created by Administrator on 2019/9/21 0021.
//

#include "jni.h"
#include "androidlog.h"
#include <stdlib.h>
#include <android/bitmap.h>
#include "string.h"
#include <setjmp.h>
#include <jpeglib.h>
#include <jpegint.h>

//声明以8位，即1字节为单位
typedef u_int8_t BYTE;

typedef struct my_error_mgr *my_error_ptr;

struct my_error_mgr {
    struct jpeg_error_mgr pub;
    jmp_buf setjmp_buffer;
};


const char * jstringToString(JNIEnv  *env,  void *dstFile_) {
    const char *temp = (*env)->GetStringUTFChars(env, dstFile_, NULL);
    jsize length = (*env)->GetStringUTFLength(env, dstFile_);
    if(length>0){
        char *temDstFile= (char *)malloc(length+1);
        memcpy(temDstFile,temp,length);
        temDstFile[length] = 0;
    }
    (*env)->ReleaseStringUTFChars(env,dstFile_,temp);
    return temp;
}
METHODDEF(void)my_error_exit(j_common_ptr cinfo) {
    my_error_ptr myerr = (my_error_ptr) cinfo->err;
    (*cinfo->err->output_message)(cinfo);
    LOGW("jpeg_message_table[%d]:%s",myerr->pub.msg_code, myerr->pub.jpeg_message_table[myerr->pub.msg_code]);
    longjmp(myerr->setjmp_buffer, 1);
}


int generateJPEG(BYTE *data, int width, int height, int quality, const char *name, boolean optimize) {
    int nComponent = 3;
    //jpeg对象
    struct jpeg_compress_struct jcs;

    //自定义的error
    struct my_error_mgr jem;

    //错误日志
    jcs.err = jpeg_std_error(&jem.pub);
    jem.pub.error_exit = my_error_exit;

    if (setjmp(jem.setjmp_buffer)) {
        return 0;
    }

    //为jpeg对象分配空间
    jpeg_create_compress(&jcs);

    //打开文件
    FILE *file = fopen(name, "wb");
    if(file==NULL){
        return 0;
    }

    //指定压缩数据源
    jpeg_stdio_dest(&jcs,file);
    jcs.image_width = (JDIMENSION)width;
    jcs.image_height = (JDIMENSION) height;
    jcs.arith_code = FALSE;//是否降采样
    jcs.input_components = nComponent;//通道数
    jcs.in_color_space = JCS_RGB;//rgb通道
    jpeg_set_defaults(&jcs);

    //是否使用哈夫曼算法
    jcs.optimize_coding = optimize;
    //为压缩设定参数，包括图像大小，颜色空间
    jpeg_set_quality(&jcs, quality, TRUE);
    //开始压缩
    jpeg_start_compress(&jcs, TRUE);
    JSAMPROW row_point[1];
    int row_stride;
    row_stride = jcs.image_width * nComponent;
    while (jcs.next_scanline < jcs.image_height) {
        row_point[0] = &data[jcs.next_scanline * row_stride];
        jpeg_write_scanlines(&jcs, row_point, 1);
    }

    if (jcs.optimize_coding) {
        LOGI("使用了哈夫曼算法完成压缩");
    } else {
        LOGI("未使用哈夫曼算法");
    }

    //压缩完毕
    jpeg_finish_compress(&jcs);
    //释放资源
    jpeg_destroy_compress(&jcs);
    fclose(file);
    return 1;
}


/**
 *  压缩入口
 */

JNIEXPORT jint JNICALL
Java_com_hechuangwu_baselibrary_helper_jpeg_ImageCompress_nativeCompressBitmap(JNIEnv *env, jclass jclz,
                                                                               jobject bitmap, jint quality,
                                                                               jstring dstFile_,
                                                                               jboolean optimize) {

    int ret;
    AndroidBitmapInfo androidBitmapInfo;
    BYTE *pixelsColor;
    BYTE *data,*tempData;
    BYTE r,g,b;//三个颜色通道,每个占8位
    int color;//颜色值
    int width,height,format;//宽高，分辨率，像素格式

    //获取bitmap信息
    ret = AndroidBitmap_getInfo(env, bitmap,&androidBitmapInfo);
    if (ret<0){
        LOGI("AndroidBitmap_getInfo Failed %d",ret)
        return ret;
    }

    //锁定像素地址
    ret = AndroidBitmap_lockPixels(env,bitmap,(void **)&pixelsColor);
    if(ret<0){
        LOGI("AndroidBitmap_getInfo Failed %d",ret)
        return ret;
    }


    width = androidBitmapInfo.width;
    height = androidBitmapInfo.height;
    format = androidBitmapInfo.format;

    LOGI("bitmap: width=%d,height=%d,size=%d , format=%d ",width, height,width* height,format);

    data = (BYTE *)malloc(width*height*3);
    tempData = data;
    //bitmap转rgb
    for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
            if(format == ANDROID_BITMAP_FORMAT_RGBA_8888){
                //像素数据
                color = *((int*)(pixelsColor));
                //取出rgb数据，原数据排列顺序是A B G R
                b = (color>>16)& 0xFF;
                g = (color >> 8) & 0xFF;
                r = (color >> 0) & 0xFF;

                //将数据填入分配的数据空间
                *data = r;
                *(data + 1) = g;
                *(data + 2) = b;
                //一次填满指向像素末端
                data += 3;
                //去掉透明通道
                pixelsColor += 4;
            } else{
                return  -2;
            }
        }
    }

    //释放锁
    AndroidBitmap_unlockPixels(env,bitmap);

    //目标文件
    const char *dstFileName = jstringToString(env, dstFile_);

    //将数据进行压缩
    generateJPEG(tempData,width,height,quality,dstFileName,optimize);

    free((void *)dstFileName);
    free((void *)tempData);
    return ret;
}