package me.winds.album.tools;

import android.graphics.Bitmap;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author by Winds on 2016/11/2 0002.
 * Email heardown@163.com.
 */

/**
 * 包装类
 */
public class IrSurveyTools {
//    /**
//     * 包装类 获取原始图片
//     *
//     * @param data
//     * @return
//     */
//    public static Observable<Bitmap> getOriginalBitmap(final byte[] data) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                if (!subscriber.isUnsubscribed()) {
//                    Bitmap bitmap = ImageTools.getOriginalBitmap(data, 60, 80);
//                    if (bitmap != null) {
//                        subscriber.onNext(ImageTools.rotateBitmap(bitmap, 90));
//                    } else {
//                        subscriber.onError(null);
//                    }
//                }
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//    }

//    public static Observable<SoftReference<Bitmap>> getOriginalReference(byte[] data, final boolean bool) {
//        return Observable.just(data)
//                .map(new Func1<byte[], Bitmap>() {
//                    @Override
//                    public Bitmap call(byte[] bytes) {
//                        Bitmap bitmap = Bitmap.createBitmap(80, 60, Bitmap.Config.RGB_565);
//                        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//                        bitmap.copyPixelsFromBuffer(buffer);
//                        return bitmap;
//                    }
//                })
//                .map(new Func1<Bitmap, SoftReference<Bitmap>>() {
//                    @Override
//                    public SoftReference<Bitmap> call(Bitmap bitmap) {
//                        if (bitmap != null) {
//                            Bitmap bmp = ImageTools.rotateBitmap(bitmap, 90);
//
////
////                            if (bool) {
////                                save(bmp);
////                            }
//                            return new SoftReference<Bitmap>(bmp);
//                        }
//                        return null;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

//    /**
//     * @param data   图片数据
//     * @param bool   是否保存
//     * @param row    原始坐标 left
//     * @param col    原始坐标 top
//     * @param state  十字光标是否关闭 1 关闭 0 开启
//     * @param heat   温度
//     * @param unit   温度单位 0 摄氏度  1 华氏度
//     * @param stable 温度值是否稳定 1 稳定  0 不稳定
//     * @return
//     */
//    public static Observable<SoftReference<Bitmap>> getOriginalReference(byte[] data, final boolean bool, final int row, final int col, final int state, final String heat, final int unit, final int stable) {
//        return Observable.just(data)
//                .map(new Func1<byte[], Bitmap>() {
//                    @Override
//                    public Bitmap call(byte[] bytes) {
//                        Bitmap bitmap = Bitmap.createBitmap(80, 60, Bitmap.Config.RGB_565);
//                        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//                        bitmap.copyPixelsFromBuffer(buffer);
//                        return bitmap;
//                    }
//                })
//                .map(new Func1<Bitmap, SoftReference<Bitmap>>() {
//                    @Override
//                    public SoftReference<Bitmap> call(Bitmap bitmap) {
//                        if (bitmap != null) {
//                            Bitmap bmp = ImageTools.rotateBitmap(bitmap, 90);
//                            return new SoftReference<Bitmap>(bmp);
//                        }
//                        return null;
//                    }
//                })
//                .doOnNext(new Action1<SoftReference<Bitmap>>() {
//                    @Override
//                    public void call(SoftReference<Bitmap> reference) {
////                        if (reference.get() != null) {
//                        if (bool && reference.get() != null) {
//                            saveBitmap2File(reference.get(), heat, unit, stable);
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io());
//    }

    public static void saveBitmap2File(Bitmap bmp, final String heat, final int unit, final int stable) {
        Observable.just(bmp)
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        if (unit == 0) { //摄氏度       //温度单位 0 摄氏度  1 华氏度
//                            return ImageTools.drawTextToLeftTop(bitmap, stable == 1 ? StringUtils.join(heat, "℃") : StringUtils.join("≈", heat, "℃"), 4, Color.WHITE, 3, 3);
//                        } else {    //华氏度
//                            return ImageTools.drawTextToLeftTop(bitmap, stable == 1 ? StringUtils.join(heat, "℉") : StringUtils.join("≈", heat, "℉"), 4, Color.WHITE, 3, 3);
//                        }
//                    }
//                })
                .map(new Func1<Bitmap, Void>() {
                    @Override
                    public Void call(Bitmap bitmap) {
                        FileUtils.saveBitmap2File(bitmap);
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }


    public static Observable<String> copyFile(final String from, final String to) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext(FileUtils.copyFile(from, to));
                subscriber.onCompleted();
            }
        });
    }


//    /**
//     * 包装类 把图片字节数据根据宽高解析成图片，并保存
//     *
//     * @param data
//     * @param width
//     * @param height
//     * @param dir
//     * @return 图片的保存地址
//     */
//    public static Observable<String> getOriginalBitmapPath(final byte[] data, final int width, final int height, final String dir) {
//        return Observable.just(data)
//                .map(new Func1<byte[], Bitmap>() {
//                    @Override
//                    public Bitmap call(byte[] bytes) {
//                        return ImageTools.getOriginalBitmap(data, height, width);
//                    }
//                })
//                .map(new Func1<Bitmap, String>() {
//                    @Override
//                    public String call(Bitmap bitmap) {
//                        String path = PathUtils.buildDir(dir) + File.separator + System.currentTimeMillis() + ".jpeg";
//                        ImageTools.saveBitmap2File(bitmap, path);
//                        return path;
//                    }
//                });
//    }
//
//
//    public static Observable<Bitmap> getFlawlessBitmap(final Context context, final byte[] image, final String heat, final int row, final int col) {
//        return Observable.just(image)
//                .map(new Func1<byte[], int[]>() {
//                    @Override
//                    public int[] call(byte[] bytes) {
//                        //获取像素点
//                        int[] pixels = new int[4800];
//                        Bitmap temp = ImageTools.getOriginalBitmap(image, 60, 80);
//                        Bitmap bitmap = ImageTools.rotateBitmap(temp, 90);
//                        bitmap.getPixels(pixels, 0, 60, 0, 0, 60, 80);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], int[]>() {
//                    @Override
//                    public int[] call(int[] ints) {
//                        //获取放大后的像素点
//                        int[] pixels = BilineInterpolationScale.imgScale(ints, 60, 80, 360, 480);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], Bitmap>() {
//                    @Override
//                    public Bitmap call(int[] ints) {
//                        //生成放大后的图片
//                        Bitmap bitmap = Bitmap.createBitmap(ints, 0, 360, 360, 480, Bitmap.Config.RGB_565);
//                        return bitmap;
//                    }
//                })
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        //生成文字水印
//
//                        Bitmap bmp = ImageTools.drawTextToLeftTop(bitmap, heat, 20, Color.WHITE, 20, 10);
//                        return bmp;
//                    }
//                })
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        //生成图片水印
//                        Bitmap water = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cursor_write);
//                        return ImageTools.createWaterMaskBitmap(bitmap, water, col * 6, row * 6);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io());
//    }
//
//    public static Observable<Boolean> getFlawlessBitmap(final Context context, final byte[] image, final String heat,
//                                                        final int row, final int col, final int state, final int unit, final int stable) {
//        return Observable.just(image)
//                .map(new Func1<byte[], int[]>() {
//                    @Override
//                    public int[] call(byte[] bytes) {
//                        //获取像素点
//                        int[] pixels = new int[4800];
//                        Bitmap temp = ImageTools.getOriginalBitmap(image, 60, 80);
//                        Bitmap bitmap = ImageTools.rotateBitmap(temp, 90);
//                        bitmap.getPixels(pixels, 0, 60, 0, 0, 60, 80);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], int[]>() {
//                    @Override
//                    public int[] call(int[] ints) {
//                        //获取放大后的像素点
//                        int[] pixels = BilineInterpolationScale.imgScale(ints, 60, 80, 360, 480);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], Bitmap>() {
//                    @Override
//                    public Bitmap call(int[] ints) {
//                        //生成放大后的图片
//                        Bitmap bitmap = Bitmap.createBitmap(ints, 0, 360, 360, 480, Bitmap.Config.RGB_565);
//                        return bitmap;
//                    }
//                })
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        //生成文字水印
//
//                        if (unit == 0) { //摄氏度       //温度单位 0 摄氏度  1 华氏度
//                            return ImageTools.drawTextToLeftTop(bitmap, stable == 1 ? StringUtils.join(heat, "℃") : StringUtils.join("≈", heat, "℃"), 20, Color.WHITE, 20, 10);
//                        } else {    //华氏度
//                            return ImageTools.drawTextToLeftTop(bitmap, stable == 1 ? StringUtils.join(heat, "℉") : StringUtils.join("≈", heat, "℉"), 20, Color.WHITE, 20, 10);
//                        }
//
//                    }
//                })
//                .map(new Func1<Bitmap, SoftReference<Bitmap>>() {
//                    @Override
//                    public SoftReference<Bitmap> call(Bitmap bitmap) {
//                        //生成图片水印
//                        if (state == 0) {
//                            Bitmap water = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_cursor_write);
//                            bitmap = ImageTools.createWaterMaskBitmap(bitmap, water, col * 6 - water.getWidth() / 2, row * 6 - water.getHeight() / 2);
//                        }
//                        return new SoftReference<Bitmap>(bitmap);
//                    }
//                })
//                .map(new Func1<SoftReference<Bitmap>, Boolean>() {
//                    @Override
//                    public Boolean call(SoftReference<Bitmap> reference) {
//                        String s = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + GloableValue.FOLDER_DIR
//                                + File.separator + GloableValue.FOLDER_IMAGE + File.separator
//                                + CommonTools.getPreference(context, GloableValue.SP_IMAGE_FOLDER, context.getString(R.string.default_image_folder));
//                        String dir = PathUtils.buildDir(s);
//                        if (reference != null && reference.get() != null) {
//                            if (dir != null) {
//                                ImageTools.saveBitmap2File(reference.get(), dir + File.separator + System.currentTimeMillis() + ".jpeg");
//                                return true;
//                            }
//                        }
//
//                        return false;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public static Observable<Bitmap> getFlawlessBitmap(final byte[] image, final Bitmap water, final String heat, final int row, final int col) {
//        return Observable.just(image)
//                .map(new Func1<byte[], int[]>() {
//                    @Override
//                    public int[] call(byte[] bytes) {
//                        //获取像素点
//                        int[] pixels = new int[4800];
//                        Bitmap temp = ImageTools.getOriginalBitmap(image, 60, 80);
//                        Bitmap bitmap = ImageTools.rotateBitmap(temp, 90);
//                        bitmap.getPixels(pixels, 0, 60, 0, 0, 60, 80);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], int[]>() {
//                    @Override
//                    public int[] call(int[] ints) {
//                        //获取放大后的像素点
//                        int[] pixels = BilineInterpolationScale.imgScale(ints, 60, 80, 360, 480);
//                        return pixels;
//                    }
//                })
//                .map(new Func1<int[], Bitmap>() {
//                    @Override
//                    public Bitmap call(int[] ints) {
//                        //生成放大后的图片
//                        Bitmap bitmap = Bitmap.createBitmap(ints, 0, 360, 360, 480, Bitmap.Config.RGB_565);
//                        return bitmap;
//                    }
//                })
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        //生成文字水印
//                        Bitmap bmp = ImageTools.drawTextToLeftTop(bitmap, heat, 22, Color.WHITE, 20, 10);
//                        return bmp;
//                    }
//                })
//                .map(new Func1<Bitmap, Bitmap>() {
//                    @Override
//                    public Bitmap call(Bitmap bitmap) {
//                        //生成图片水印
//                        return ImageTools.createWaterMaskBitmap(bitmap, water, row, col);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    /**
//     * 生成水印图片
//     *
//     * @param image
//     * @param water
//     * @return
//     */
//    public static Observable<Bitmap> getIntactBitmap(final byte[] image, final Bitmap water) {
//        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                if (!subscriber.isUnsubscribed()) {
//
//                    int[] pixels = new int[4800];
//                    //获取像素点
//                    ImageTools.rotateBitmap(ImageTools.getOriginalBitmap(image, 60, 80), 90).getPixels(pixels, 0, 60, 0, 0, 60, 80);
//                    //获取放大后的像素点
//                    int[] newPixels = BilineInterpolationScale.imgScale(pixels, 60, 80, 360, 480);
//                    //生成放大后的图片
//                    Bitmap bitmap = Bitmap.createBitmap(newPixels, 0, 360, 360, 480, Bitmap.Config.RGB_565);
//                    //生成文字水印
//                    Bitmap temp = ImageTools.drawTextToLeftTop(bitmap, TimeUtils.getFormatTime("yyyy-MM-dd HH:mm"), 22, Color.BLACK, 20, 10);
//                    //生成图片水印
//                    Bitmap bmp = ImageTools.createWaterMaskBitmap(temp, water, (temp.getWidth() - water.getWidth()) / 2, (temp.getHeight() - water.getHeight()) / 2);
//
//                    subscriber.onNext(bmp);
//                    subscriber.onCompleted();
//
//                }
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
}
