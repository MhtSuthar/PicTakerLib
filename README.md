# PicTakerLib
Image Picker From Camera and Gallary

Step 1 : We are asuming that you have all permission allow for camera and read & write external storage.

step 2 : Inside your Activity onCreate() method
```gradle
PicTake mPicTake = new PicTake(this);
```

step 3 : whenever you want to open Camera & gallery call this method
```gradle
mPicTake.build();
```

step 4 : Ovveride onActivityResult method and call like this and get image path
```gradle
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String mPath = mPicTake.getImagePathFromResult(this, requestCode, resultCode, data);
        Log.e(TAG, "Image path "+mPath);
    }
```
    

Add it to your build.gradle with:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and:
```gradle
dependencies {
    compile 'com.github.MhtSuthar:PicTakerLib:1.11'
}
```
