package com.softwareproject.chandy.teller.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.softwareproject.chandy.teller.Application.MyApplication;
import com.softwareproject.chandy.teller.Bean.Setting;
import com.softwareproject.chandy.teller.DB.DBManager;
import com.softwareproject.chandy.teller.R;
import com.softwareproject.chandy.teller.databinding.ActivitySettingBinding;

/**
 * Created by Chandy on 2016/12/4.
 */

public class SettingActivity extends Activity {
    ActivitySettingBinding binding;
    DBManager dbManager;
    Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setActivity(this);
        dbManager = new DBManager(this);
        setting=MyApplication.getSetting();
        initData();
    }

    public void cleanHistory(View view) {
        //首先弹出dialog询问是否删除防止误按
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
        dialog.setTitle("注意");
        dialog.setMessage("如果删除历史记录将无法再找回，是否继续？");
        dialog.setIcon(R.mipmap.denger);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingActivity.this, "delete", Toast.LENGTH_SHORT).show();
                DBManager dbManager=new DBManager(SettingActivity.this);
                dbManager.dropTable();
                dbManager.closeDB();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingActivity.this, "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }

    public void writeInFile(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("setting", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        switch (view.getId()){
            case R.id.alert:
                edit.putBoolean("alert",((CheckBox)view).isChecked());
                break;
            case R.id.alwaysOn:
                edit.putBoolean("alwaysOn",((CheckBox)view).isChecked());
                break;
            case R.id.shake:
                edit.putBoolean("shake",((CheckBox)view).isChecked());
                break;
            case R.id.voice:
                edit.putBoolean("voice",((CheckBox)view).isChecked());
                break;
        }
        edit.apply();
    }

    private void initData(){
        SharedPreferences sharedPreferences = getSharedPreferences("setting", getApplicationContext().MODE_PRIVATE);
        binding.alert.setChecked(sharedPreferences.getBoolean("alert",true));
        binding.alwaysOn.setChecked(sharedPreferences.getBoolean("alwaysOn",true));
        binding.shake.setChecked(sharedPreferences.getBoolean("shake",true));
        binding.voice.setChecked(sharedPreferences.getBoolean("voice",true));
    }

    public void aboutUs(View view){
        Toast.makeText(SettingActivity.this,"小组成员：常昊，谢志东，郭鑫璐，常春，周冲浩",Toast.LENGTH_LONG).show();
    }

    public void sendBug(View view){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:chandy1994c@gmail.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "There is a Bug");
        data.putExtra(Intent.EXTRA_TEXT, "I had found a bug ..........");
        startActivity(data);
    }
}
