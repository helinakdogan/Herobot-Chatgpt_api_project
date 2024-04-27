package com.nexis.herobot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    // oncreate: app başlatıldığında bir kere çalışan fonksiyon, eventlistener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // mesajlar için arraylist
        messageList = new ArrayList<>();

        // main xmldeki arayüz elemanlarını çağırıyor
        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);


        // recylerView setup

        // calls MessageAdapter class
        messageAdapter = new MessageAdapter(messageList);
        // recylerView'a bi tane messageAdapter class örneği atadı
        recyclerView.setAdapter(messageAdapter);

        // recylerView'a dikey düzen sağlıyor, recylerView'un zımbırtısı
        LinearLayoutManager llm = new LinearLayoutManager(this);
        // yeni mesaj altta gözükecek
        llm.setStackFromEnd(true);
        // linearlayoutmanagerı recyclerviewa atadık
        recyclerView.setLayoutManager(llm);


        sendButton.setOnClickListener((v) -> {
            // get edit text and turn into string
            String question = messageEditText.getText().toString().trim();
            // add my message to chat
            addToChat(question, Message.SENT_BY_ME);
            // reset edit text
            messageEditText.setText("");
            // send my message to chatgpt api and get response from it
            callAPI(question);
            // welcome text -> invisible
            welcomeTextView.setVisibility(View.GONE);
        });
    }

    void addToChat(String message, String sentBy) {
        // multithreading for messaging
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // create new item for messageList
                messageList.add(new Message(message, sentBy));
                // notify messageAdapter, messageList has changed
                messageAdapter.notifyDataSetChanged();
                // scroll recyclerView -> new message is visible
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        // remove "Typing..." from chat
        messageList.remove(messageList.size() - 1);
        // add response to chat
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String question) {
        //okhttp

        // show "Typing..." message to messageList, it will show it until getting response
        messageList.add(new Message("Typing... ", Message.SENT_BY_BOT));


        // create Json object to send response in json format
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");  // chatgpt model
            jsonBody.put("prompt", question);  // our text to send
            jsonBody.put("max_tokens", 4000);  // max character of text
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            e.printStackTrace(); // if there is exception, show it on console
        }
        // json object in String format, body
        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        // send this body request to api
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions") // url we send
                .header("Authorization", "Bearer YOUR_API_KEY") // API key
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {

            // if response doesn't come
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            // if response comes
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // if the response is successful (if the HTTP status code is in the range of 200-299)
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        // response comes in json format, turned into String
                        jsonObject = new JSONObject(response.body().string());
                        // extracts the "choices" array from the JSON object, it provides response options
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        // put the first object in the "choices" array into result
                        String result = jsonArray.getJSONObject(0).getString("text");
                        // add result to messageList
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body().toString());
                }
            }
        });
    }
}



