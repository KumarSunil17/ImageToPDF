package com.abhijeet14.camdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    Bitmap b;
    String directoryPath;
    Document document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);

    }

    public void capture(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            b = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(b);
        }else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public void exportPDF(View view) {
        document = new Document(PageSize.A4, 50, 50, 50, 50);

        directoryPath = android.os.Environment.getExternalStorageDirectory().toString();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/example.pdf"));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        Image image = null;
        try {
            image = Image.getInstance(directoryPath + "/" + "example.jpg");
            image.setAlignment(Element.ALIGN_CENTER);

            Float width = document.getPageSize().getWidth()-50.0f;
            Float height = document.getPageSize().getHeight()-50.0f;

            Rectangle rectangle = new Rectangle(width,height);
            image.setAlignment(Element.ALIGN_CENTER);
            image.scaleAbsolute(rectangle);

        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            document.add(image);
            Toast.makeText(this, "Exported", Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    public void showPDF(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(directoryPath+"/example.pdf"),"application/pdf");
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
    }

}
