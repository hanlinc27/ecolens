/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.ml.md.java.productsearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.md.java.LiveObjectDetectionActivity;
import com.google.firebase.ml.md.java.objectdetection.DetectedObject;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** A fake search engine to help simulate the complete work flow. */
public class SearchEngine {

  private static final String TAG = "SearchEngine";
  private static String successful_label = "";
  public static String formatted_label = "";

  public interface SearchResultListener {
    void onSearchCompleted(DetectedObject object, List<Product> productList);
  }

  private final RequestQueue searchRequestQueue;
  private final ExecutorService requestCreationExecutor;

  public SearchEngine(Context context) {
    searchRequestQueue = Volley.newRequestQueue(context);
    requestCreationExecutor = Executors.newSingleThreadExecutor();
  }

  public void search(DetectedObject object, SearchResultListener listener) {
    // Crops the object image out of the full image is expensive, so do it off the UI thread.
    Tasks.call(requestCreationExecutor, () -> createRequest(object))
        .addOnSuccessListener(productRequest -> {
            List<Product> list = new ArrayList<>();
            listener.onSearchCompleted(object, list);
        })
        .addOnFailureListener(
            e -> {
              Log.e(TAG, "Failed to create product search request!", e);
              // Remove the below dummy code after your own product search backed hooked up.
              List<Product> productList = new ArrayList<>();
              for (int i = 0; i < 8; i++) {
                productList.add(
                    new Product(/* imageUrl= */ "", "Product title " + i, "Product subtitle " + i));
              }
              listener.onSearchCompleted(object, productList);
            });
  }

  private static String createRequest(DetectedObject searchingObject) throws Exception {
      Bitmap objectImageData = searchingObject.getBitmap();

    if (objectImageData == null) {
      throw new Exception("Failed to get object image data!");
    }

    FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
            .setAssetFilePath("model/manifest.json").build();

    FirebaseVisionImageLabeler labeler;
    try {
      FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
              new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                      .setConfidenceThreshold(0.3f)  // Evaluate your model in the Firebase console
                      // to determine an appropriate value.
                      .build();
      labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);

      FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(objectImageData);

      labeler.processImage(image)
              .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                @Override
                public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                  // Task completed successfully
                  System.out.println("nice");

                  for (FirebaseVisionImageLabel label: labels){
                    String text = label.getText();
                    successful_label = labels.get(0).getText();
                 }
                    formatted_label = display_name(successful_label);
                    LiveObjectDetectionActivity.bottomSheetButton.setText(formatted_label);
                }
              })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  // Task failed with an exception
                  // ...
                }
              });
    } catch (FirebaseMLException e) {
      // ...
    }

    return successful_label;
  }

  public void shutdown() {
    searchRequestQueue.cancelAll(TAG);
    requestCreationExecutor.shutdown();
  }

  public static String display_name(String label) {
      if (label.equals("plastic_cups"))
          return "Plastic Cups";
      else if (label.equals("water_bottle_caps"))
          return "Bottle Caps";
      else if (label.equals("milk_cartons"))
          return "Milk Cartons";
      else if (label.equals("paper_coffee_cups"))
          return "Coffee Cups";
      else if (label.equals("soda_cans"))
          return "Soda Cans";
      else if (label.equals("plastic_bags"))
          return "Plastic Bags";
      else if (label.equals("plastic_bottles"))
          return "Plastic Bottles";
      else return "";
  }

}
