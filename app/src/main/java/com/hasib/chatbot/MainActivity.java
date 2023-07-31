package com.hasib.chatbot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hasib.chatbot.Controller.MessageAdapter;
import com.hasib.chatbot.Model.Message;
import com.hasib.chatbot.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private EditText editText;
    private ImageButton sendBtn;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    public static final MediaType apiJson
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        editText = binding.typeMessage;
        sendBtn = binding.sendBtn;
        recyclerView = binding.recyclerView;

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendBtn.setOnClickListener((v) -> {
                String question = editText.getText().toString();
                addToChat(question, Message.MY_MSG);
                chatBotApi(question);
                editText.setText("");
        });


    }

    //addToChat method
     @SuppressLint("NotifyDataSetChanged")
     private void addToChat(String question, String sender){        runOnUiThread(() -> {
            messageList.add(new Message(question, sender, getTime()));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });

    }

    //Date Time picker
    private String getTime(){
        //date time
        Date currentTime = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        return sdf.format(currentTime);
    }

    //response method
    private void addResponse(String msg){
        messageList.remove(messageList.size()-1);
        addToChat(msg, Message.BOT_MSG);
    }
    //chatBotApi method
    private void chatBotApi(String question){
        messageList.add(new Message("Typing...", Message.BOT_MSG,""));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", question);
            jsonObject.put("max_tokens", 1000);
            jsonObject.put("temperature", 0);
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), apiJson);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer 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")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    addResponse("Failed to load response due to "+ Objects.requireNonNull(response.body()).toString());
                }
            }
        });

    }
}
