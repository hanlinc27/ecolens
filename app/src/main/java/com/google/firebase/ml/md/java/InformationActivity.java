package com.google.firebase.ml.md.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.ml.md.R;
import com.google.firebase.ml.md.java.productsearch.SearchEngine;

import java.util.Random;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    Button project;
    Button info;
    Button back;

    TextView title;

    String label;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        project = findViewById(R.id.make_button);
        project.setOnClickListener(this);
        info = findViewById(R.id.learn_button);
        info.setOnClickListener(this);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(this);

        title = findViewById(R.id.textView2);
        title.setText(SearchEngine.formatted_label);
    }

    @Override
    public void onClick (View view) {
        int id = view.getId();
        if (id==R.id.make_button) {
            label = SearchEngine.formatted_label;
            Intent viewIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(choose_url(label)));
            startActivity(viewIntent);
        } else if (id==R.id.learn_button) {

        } else if (id==R.id.back_button)
            finish();
    }

    public String choose_url (String label) {
        Random rand = new Random();
        int random_num = rand.nextInt(3);

        label = label.toLowerCase();
        System.out.println(label);

        if (label.equals("plastic cups")) {
            String[] urls = {"https://www.pinterest.ca/pin/142144931976375519/",
                    "https://www.pinterest.ca/pin/419468152792000185/",
                    "https://www.pinterest.ca/pin/346777240052712947/"};
            return urls[random_num];
        }
        else if (label.equals("Bottle Caps")) {
            String[] urls = {"https://www.pinterest.ca/pin/8866530487731454/",
                    "https://www.pinterest.ca/pin/592856738431291976/",
                    "https://www.pinterest.ca/pin/117023290299379688/"};
            return urls[random_num];
        }
        else if (label.equals("milk cartons")) {
            String[] urls = {"https://www.pinterest.ca/pin/444871269439691686/",
                    "https://www.pinterest.ca/pin/491596115578153415/",
                    "https://www.pinterest.ca/pin/576953402249463674/"};
            return urls[random_num];
        }
        else if (label.equals("coffee cups")) {
            String[] urls = {"https://www.pinterest.ca/pin/776589529479068915/",
                    "https://www.pinterest.ca/pin/528117493805915250/",
                    "https://www.pinterest.ca/pin/570549846543387956/" };
            return urls[random_num];
        }
        else if (label.equals("soda cans")) {
            String[] urls = {"http://www.lovefrombe.com/2013/07/decorative-camera.html#.XjXroOLQiu6",
                    "https://www.diyhsh.com/2011/03/do-you-have-picky-eater.html",
                    "https://www.theidearoom.net/diy-soda-can-coasters"};
            return urls[random_num];
        }
        else if (label.equals("plastic bags")) {
            String[] urls = {"https://www.pinterest.ca/pin/AbgrVBaWdFmfCmmbzA8Mefe0PmrUJwK_8PJBcGsav250kkzdbeZw5qY/",
                    "https://www.pinterest.ca/pin/754001162592202384/",
                    "https://www.pinterest.ca/pin/765049055429591986/"};
            return urls[random_num];
        }
        else if (label.equals("plastic bottles")) {
            String[] urls = {"https://www.pinterest.ca/pin/732327589387624333/",
                    "https://www.kitchentableclassroom.com/weaving-for-kids-a-recycled-plastic-bottle-craft/",
                    "http://www.lovefrombe.com/2013/07/decorative-camera.html#.XjXr7OLQiu6"};
            return urls[random_num];
        }

        return "";
    }

}
