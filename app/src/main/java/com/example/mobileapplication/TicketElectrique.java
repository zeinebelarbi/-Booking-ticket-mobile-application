package com.example.mobileapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TicketElectrique extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;
    private Button BtnGetTicket;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int i=0,iA=5,iB=8,iC=12,iAa=2,iBa=1,iCa=3,ia,iX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_electrique);
        BtnGetTicket=findViewById(R.id.BtnGetTicket);
        drawerLayout=findViewById(R.id.drawer_layout_ticket);
        navigationView=findViewById(R.id.navigation_view_ticket);
        menuIcon=findViewById(R.id.menu_ticket);
        BtnGetTicket=findViewById(R.id.BtnGetTicket);
        radioGroup=findViewById(R.id.radioGroup);
        BtnGetTicket.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String nameBillet = radioButton.getText().toString();
        try {
            createPdf(nameBillet);
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
    }
    });
        navigationDrawer();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.add:
                        startActivity(new Intent(TicketElectrique.this,PrincipalActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.profilUserActivity:
                        startActivity(new Intent(TicketElectrique.this,ProfilActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.ticketElectrique:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createPdf(String nameBillet) throws FileNotFoundException {
   i++;
   String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    File file =new File (pdfPath ,  "TicketElectric"+i+".pdf");
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(5,5,5,5);

        Drawable d = getDrawable(R.drawable.logo);
        Bitmap bitmap =((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        Paragraph title =new Paragraph("Ticket Electric").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20);
        Paragraph welcome =new Paragraph("welcome").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(17);

        Paragraph nameTicket,numTicket;
        if (nameBillet.equals("Choix A")) {
           iA++;
           nameTicket = new Paragraph("Choix A").setTextAlignment(TextAlignment.CENTER).setFontSize(15);
           numTicket = new Paragraph("A0"+iA).setTextAlignment(TextAlignment.CENTER).setFontSize(13);
           ia=iAa;
           iX=iA;
        } else if (nameBillet.equals("Choix B")) {
            iB++;
            nameTicket = new Paragraph("Choix A").setTextAlignment(TextAlignment.CENTER).setFontSize(15);
            numTicket = new Paragraph("B0"+iB).setTextAlignment(TextAlignment.CENTER).setFontSize(13);
            ia=iBa;
            iX=iB;
        } else if (nameBillet.equals("Choix C")) {
            iC++;
            nameTicket = new Paragraph("Choix C").setTextAlignment(TextAlignment.CENTER).setFontSize(15);
            numTicket = new Paragraph("B0"+iC).setTextAlignment(TextAlignment.CENTER).setFontSize(13);
            ia=iCa;
            iX=iC;
        }
       float[] width ={100f,100f};
        Table table =new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateTimeFormatter=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        table.addCell(new Cell().add(new Paragraph("Date")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateTimeFormatter).toString())));
        }
        DateTimeFormatter timeFormatter=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeFormatter=DateTimeFormatter.ofPattern("HH:mm:ss");
        }
        table.addCell(new Cell().add( new Paragraph("time")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add( new Paragraph(LocalTime.now().format(timeFormatter).toString())));
        }
        table.addCell((new Cell().add(new Paragraph("Num actuel"))));
        table.addCell((new Cell().add(new Paragraph(""+ia))));
        table.addCell(new Cell().add(new Paragraph("estimated time") ));
        table.addCell(new Cell().add(new Paragraph(""+(iX-ia)*60+"sc") ));

        document.add(image);
        document.add(welcome);
        document.add(table);
        document.close();
        Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show();
    }



    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.ticketElectrique);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else drawerLayout.openDrawer((GravityCompat.START));

            }
        });

        drawerLayout.setScrimColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}