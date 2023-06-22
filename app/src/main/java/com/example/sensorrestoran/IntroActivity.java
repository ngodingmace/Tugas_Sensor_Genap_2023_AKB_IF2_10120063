package com.example.sensorrestoran;

//Nama: Inka Teleda Warijo
//Nim: 10120063
//Kelas: IF2

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabIndicator;
    private Button btnNext;
    private int position = 0;
    private Button btnGetStarted;
    private Animation btnAnim;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // membuat aktivitas menjadi layar penuh
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // saat aktivitas ini akan diluncurkan, perlu dicek apakah sudah pernah dibuka sebelumnya atau belum
        if (restorePrefData()) {
            // Intro sudah pernah dibuka sebelumnya, langsung ke MainActivity
            openMainActivity();
        }

        setContentView(R.layout.activity_intro);

        // menyembunyikan action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // inisialisasi tampilan
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);

        // mengisi list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Temukan Restoran Terbaik di Sekitar Anda", "Selamat datang di aplikasi Rekomendasi Restoran kami! Temukan restoran tradisional dengan hidangan autentik. Nikmati hidangan khas dari resep turun-temurun dengan cita rasa tak tertandingi. Dari masakan lokal hingga makanan khas daerah, pilihannya beragam. Sajikan diri Anda dengan pengalaman kuliner otentik dan tak terlupakan.", R.drawable.img1));
        mList.add(new ScreenItem("Fitur Pencarian dan Filter yang Andal", "Aplikasi peta kami punya fitur pencarian dan filter andal. Cari restoran sesuai kata kunci atau jenis masakan yang diinginkan. Terapkan filter jarak, harga, rating, dan fasilitas tambahan. Mencari restoran yang sempurna jadi lebih mudah dan efisien.", R.drawable.img2));
        mList.add(new ScreenItem("Detail Restoran dan Navigasi yang Mudah", "Setelah menemukan restoran menarik, lihat detail lengkapnya seperti alamat, jam buka, menu, dan ulasan pengguna. Aplikasi peta kami punya fitur navigasi yang mudah digunakan. Dapatkan petunjuk arah jelas dengan sekali sentuh. Nikmati perjalanan kuliner tanpa kesulitan mencari jalan.", R.drawable.img3));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        // listener untuk tombol next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) { // ketika mencapai halaman terakhir
                    // tampilkan tombol GETSTARTED dan sembunyikan indikator dan tombol next
                    loadLastScreen();
                }
            }
        });

        // listener untuk tablayout
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // listener untuk tombol Get Started
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // buka MainActivity
                openMainActivity();
                // juga perlu menyimpan nilai boolean ke penyimpanan sehingga saat pengguna menjalankan aplikasi lain kali
                // kita dapat mengetahui bahwa mereka telah melihat layar intro sebelumnya
                savePrefsData();
            }
        });

        // listener untuk tombol skip
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpened", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

    // menampilkan tombol GETSTARTED dan menyembunyikan indikator dan tombol next
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : menambahkan animasi ke tombol getstarted
        btnGetStarted.setAnimation(btnAnim);
    }

    private void openMainActivity() {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
