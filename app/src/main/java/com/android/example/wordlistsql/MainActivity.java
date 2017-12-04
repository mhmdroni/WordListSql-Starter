/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.wordlistsql;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 Mengimplementasikan RecyclerView yang menampilkan daftar kata dari database SQL.
 * - Mengeklik tombol keren membuka aktivitas kedua untuk menambahkan kata ke database.
 * - Mengklik tombol Edit akan membuka aktivitas untuk mengedit kata yang ada di database.
 * - Mengklik tombol Delete akan menghapus kata yang ada dari database.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;

    private WordListOpenHelper mDB;
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private int mLastPsition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new WordListOpenHelper(this);
        // Membuat Recycle View.
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // Buat mAdapter dan berikan data yang akan ditampilkan.
        mAdapter = new WordListAdapter(this, mDB);
        // Sambungkan mAdapter dengan tampilan pendaur ulang.
        mRecyclerView.setAdapter(mAdapter);
        // Beri tampilan pendaur ulang sebagai pengelola tata letak default.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tambahkan pengendali klik tindakan terapung untuk membuat entri baru.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mulai aktivitas edit kosong.
                Intent intent = new Intent(getBaseContext(), EditWordActivity.class);
                startActivityForResult(intent, WORD_EDIT);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Tambahkan kode untuk menambahkan database.
        if (requestCode == WORD_EDIT){
            if (resultCode == RESULT_OK){
                String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);

                // Update Database
                if (!TextUtils.isEmpty(word)){
                    int id = data.getIntExtra(WordListAdapter.EXTRA_ID, -99);
                    if (id == WORD_ADD){
                        mDB.insert(word);
                    } else if (id>=0){
                        mDB.update(id, word);
                    }
                    // Update UI
                    mAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(
                            getApplicationContext(), R.string.empty_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}