package com.example.practica6;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    NotificationManager notificationManager;

    static final int NOTIFICATION_ID = 0;
    static final String CHANNEL_ID = "channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Crear canal de notificación (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "practicas",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notificacion de la App");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Button boton = findViewById(R.id.boton1);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.rana, null);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap fotografia1 = bitmapDrawable.getBitmap();

                // Intent para abrir YouTube
                Intent intentPagina = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com"));
                PendingIntent pendingIntentPag = PendingIntent.getActivity(
                        MainActivity.this,
                        0,
                        intentPagina,
                        PendingIntent.FLAG_IMMUTABLE
                );

                // ✅ Intent para realizar una llamada (abre el marcador con el número)
                Intent intentLlamada = new Intent(Intent.ACTION_DIAL);
                intentLlamada.setData(Uri.parse("tel:6141331619"));
                PendingIntent pendingIntentLlamada = PendingIntent.getActivity(
                        MainActivity.this,
                        1,
                        intentLlamada,
                        PendingIntent.FLAG_IMMUTABLE
                );

                // Crear notificación
                NotificationCompat.Builder notif = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.bob)
                        .setLargeIcon(fotografia1)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(fotografia1))
                        .setContentTitle("Practica Lunes")
                        .setContentText("Las Apps Wear OS deben funcionar")
                        .addAction(R.drawable.rana, "PÁGINA", pendingIntentPag) // Acción 1: abrir YouTube
                        .addAction(R.drawable.bob, "LLAMAR", pendingIntentLlamada) // ✅ Acción 2: llamada
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                notificationManager.notify(NOTIFICATION_ID, notif.build());
            }
        });
    }
}
