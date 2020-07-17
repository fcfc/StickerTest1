package com.ice.stickertest.datalogging;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.stickertest.R;

public class FileIOActivity extends Activity {
    Button mReadButton;
    Button mWriteButton;
    Button mAppendButton;
    TextView textContent;
    TextView textInput;
    String path = "/stickertest/";
    String fileName = "test.dta";

    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_layout);

        textContent = (TextView) findViewById(R.id.textContent);
        textInput   = (TextView) findViewById(R.id.textInput);

        mReadButton = (Button) findViewById(R.id.readButton);
        mReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textContent.setText(FileReadWrite.readFile(FileIOActivity.this));
            }
        });

        mWriteButton = (Button) findViewById(R.id.writeButton);
        mWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileReadWrite.writeToFile(FileIOActivity.this, textInput.getText().toString())){
                    Toast.makeText(FileIOActivity.this,"Saved to file",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FileIOActivity.this,"File Write Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAppendButton = (Button) findViewById(R.id.appendButton);
        mAppendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FileReadWrite.appendToFile(FileIOActivity.this, textInput.getText().toString() + "\n")){
                    Toast.makeText(FileIOActivity.this,"Appended to file",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(FileIOActivity.this,"File Write Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
