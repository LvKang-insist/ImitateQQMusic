package com.admin.core.ui.camera;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.admin.core.R;
import com.admin.core.deleggate.PermissionCheckerDelegate;
import com.admin.core.util.file.FileUtil;
import com.blankj.utilcode.util.FileUtils;
import java.io.File;

/**
 * Copyright (C)
 *
 * @file: CameraHandler
 * @author: 345
 * @Time: 2019/5/9 9:54
 * @description: 照片处理类
 */
public class CameraHandler implements View.OnClickListener {
    private final AlertDialog DIALOG ;
    private final PermissionCheckerDelegate DELEGATE;

     CameraHandler( PermissionCheckerDelegate delegate) {
        this.DELEGATE = delegate;
        this.DIALOG = new AlertDialog.Builder(delegate.getContext()).create();
    }

    final void beginCameraDialog(){
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera_panel);
            window.setGravity(Gravity.BOTTOM);
            //设置动画
            window.setWindowAnimations(R.style.anim_panel_up_form_bottom);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.photodialog_btn_cancel).setOnClickListener(this);
            window.findViewById(R.id.photodialog_btn_native).setOnClickListener(this);
            window.findViewById(R.id.photodialog_btn_take).setOnClickListener(this);
        }
    }

    private String getPhotoName(){
        return FileUtil.getFileNameByTime("IMG","jpg");
    }
    /**
     * 调起系统相机
     */
    private void takePhoto(){
        //获取一个 名字,
        final String currentPhotoName = getPhotoName();
        //拍照意图
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //创建一个文件，路径为系统相册，第二个参数为名字
        final File tempFile =  new File(FileUtil.CAMERA_PHOTO_DIR,currentPhotoName);

        //如果手机 版本大于 AP24，兼容7.0以上的写法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            final ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA,tempFile.getPath());

            //向内容提供器中插入一条数据，使用ur 参数来确定要添加的表，
            //待添加的数保存在values 参数中，添加完成后 染回一个表示这条记录的url
            final Uri uri = DELEGATE.getContext().getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

            //需要将Uri 路径转化为 实际路径
            final File readFile = FileUtils.getFileByPath(FileUtil.getRealFilePath(DELEGATE.getContext(),uri));
            final Uri realUri = Uri.fromFile(readFile);

            //保存照片的路径
            CameraImageBean.getInstance().setPath(realUri);
            //将拍的照片保存在指定的 URI
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }else {
            final Uri fileUri = Uri.fromFile(tempFile);
            CameraImageBean.getInstance().setPath(fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        }
        DELEGATE.startActivityForResult(intent,RequestCode.TAKE_PHOTO);
        if (DIALOG != null){
            DIALOG.cancel();
        }
    }

    /**
     * 打开 选择图片
     */
    private void pickPhoto(){
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //打开相册
        DELEGATE.startActivityForResult(Intent.createChooser(intent,"选择获取图片的方式"),RequestCode.PICK_PHOTO);
        if (DIALOG != null){
            DIALOG.cancel();
        }
    }

    @Override
    public void onClick(View v) {
      int id = v.getId();
      if (id == R.id.photodialog_btn_take){
          takePhoto();
      }else if (id == R.id.photodialog_btn_native){
          pickPhoto();
      }else if (id == R.id.photodialog_btn_cancel){
          DIALOG.cancel();
      }
    }
}
