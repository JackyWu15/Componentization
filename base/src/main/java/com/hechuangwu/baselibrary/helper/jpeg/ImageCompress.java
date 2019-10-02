package com.hechuangwu.baselibrary.helper.jpeg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.hechuangwu.baselibrary.helper.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by cwh on 2019/9/21 0021.
 * 功能:
 */
public class ImageCompress {

    static {
        System.loadLibrary( "compress" );
    }


    /**
     * 使用native方法进行图片压缩。
     * Bitmap的格式必须是ARGB_8888 {@link android.graphics.Bitmap.Config}。
     * @param bitmap   图片数据
     * @param quality  压缩质量
     * @param dstFile  压缩后存放的路径
     * @param optimize 是否使用哈夫曼算法
     * @return 结果
     */
    public static native int nativeCompressBitmap(Bitmap bitmap, int quality, String dstFile, boolean optimize );
    public static void compress(String srcPath,String dstPathHF) {
        CompressAsyncTask compressAsyncTask = new CompressAsyncTask(srcPath,dstPathHF);
        compressAsyncTask.execute(  );
    }

    static class CompressAsyncTask extends AsyncTask<Void,Void,Void> {
        private String srcPath, dstPathHF;
        CompressAsyncTask(String srcPath, String dstPathHF) {
            this.srcPath = srcPath;
            this.dstPathHF = dstPathHF;
        }

        @Override
        protected Void doInBackground(Void... params) {

            //哈夫曼压缩
            File file = new File( srcPath );
            Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
            ImageCompress.nativeCompressBitmap( bitmap, 20, dstPathHF, false );

            //和原生压缩对比
            try {
                String dstPath = Environment.getExternalStorageDirectory() + File.separator + "compress_raw.jpg";
                bitmap.compress( Bitmap.CompressFormat.JPEG, 20, new FileOutputStream( dstPath ) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Utils.showToast( "压缩完成" );
        }

    }

}
