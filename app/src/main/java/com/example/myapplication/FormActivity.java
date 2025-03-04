package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.databinding.ActivityFormBinding;

public class FormActivity extends AppCompatActivity {
    private AppDB db;
    private ActivityFormBinding binding;
    private Post post;
    PostDao postDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDB.class, "FooDB")
                .allowMainThreadQueries().build();
        postDao = db.postDao();
        handleSave();
        if (getIntent().getExtras() != null){
            int id = getIntent().getExtras().getInt("id");
            post = postDao.get(id);
            binding.etContent.setText(post.getContent());
        }
    }
    private void handleSave() {
        binding.btnSave.setOnClickListener(view -> {
            if (post == null) {
                post = new Post(binding.etContent.getText().toString());
                postDao.insert(post);
            }
            else {
                post.setContent(binding.etContent.getText().toString());
                postDao.update(post);
            }
            finish();
        });
    }
}