package com.gk.smgadmin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gk.smgadmin.R;
import com.gk.smgadmin.adapters.TodayResultAdapter;
import com.gk.smgadmin.databinding.ActivityUpdateTodayResultBinding;
import com.gk.smgadmin.models.ApiInterface;
import com.gk.smgadmin.models.ApiWebServices;
import com.gk.smgadmin.models.MessageModel;
import com.gk.smgadmin.models.PageViewModel;
import com.gk.smgadmin.models.TodayResultModel;
import com.gk.smgadmin.utils.CommonMethod;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTodayResultActivity extends AppCompatActivity implements TodayResultAdapter.TodayResultInterface {
    ActivityUpdateTodayResultBinding binding;
    List<TodayResultModel> resultModelList = new ArrayList<>();
    TodayResultAdapter todayResultAdapter;
    PageViewModel pageViewModel;
    Dialog loadingDialog,todayResultDialog;
    ApiInterface apiInterface;
    EditText todayResultTitleTxt,resultTimeTxt,nextNoTxt;
    Button uploadTodayResultBtn,cancelBtn;
    Map<String,String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTodayResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiInterface = ApiWebServices.getApiInterface();
        loadingDialog = CommonMethod.getLoadingDialog(this);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.todayResultRecyclerview.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.todayResultRecyclerview);

        fetchTodayResult();
    }

    private void fetchTodayResult() {
        loadingDialog.show();
        pageViewModel.getTodayResultList().observe(this, todayResultModelList -> {
            if (todayResultModelList != null) {
                resultModelList.clear();
                resultModelList.addAll(todayResultModelList.getData());
                todayResultAdapter = new TodayResultAdapter(this);
                binding.todayResultRecyclerview.setAdapter(todayResultAdapter);
                todayResultAdapter.updateTodayResultList(resultModelList);
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClicked(TodayResultModel todayResultModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Edit or Delete");
        builder.setMessage("Edit or Delete your Item.");
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        }).setPositiveButton("Delete", (dialog, which) -> {
            loadingDialog.show();
            String itemId = todayResultModel.getId();
            map.put("id",itemId);
            map.put("title","result");
            deleteData(map);
        }).setNeutralButton("Edit", (dialog, which) -> {
            String itemId = todayResultModel.getId();
            String resTitle = todayResultModel.getResName();
            String resTime = todayResultModel.getResultTime();
            String nextNo = todayResultModel.getNewNo();

            uploadTodayResultDialog(itemId,resTitle,resTime,nextNo);
        }).show();
    }

    private void uploadTodayResultDialog(String itemId, String resTitle, String resTime, String nextNo) {
        todayResultDialog = new Dialog(this);
        todayResultDialog.setContentView(R.layout.today_result_layout);
        todayResultDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        todayResultDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.item_bg));
        todayResultDialog.setCancelable(false);
        todayResultDialog.show();

        todayResultTitleTxt = todayResultDialog.findViewById(R.id.today_title);
        resultTimeTxt = todayResultDialog.findViewById(R.id.result_time);
        nextNoTxt = todayResultDialog.findViewById(R.id.new_no);

        todayResultTitleTxt.setText(resTitle);
        resultTimeTxt.setText(resTime);
        nextNoTxt.setText(nextNo);

        uploadTodayResultBtn = todayResultDialog.findViewById(R.id.upload_today_result_btn);
        cancelBtn = todayResultDialog.findViewById(R.id.today_cancel);
        cancelBtn.setOnClickListener(v -> {
            todayResultDialog.dismiss();
        });

        uploadTodayResultBtn.setOnClickListener(v -> {
            loadingDialog.show();
            String resultTitle = todayResultTitleTxt.getText().toString().trim();
            String rTime = resultTimeTxt.getText().toString().trim();
            String newNo = nextNoTxt.getText().toString().trim();

            if (TextUtils.isEmpty(resultTitle)) {
                todayResultTitleTxt.setError("Title Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(rTime)) {
                resultTimeTxt.setError("Time Required");
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(newNo)) {
                nextNoTxt.setError("Number Required");
                loadingDialog.dismiss();
            } else {
                map.put("id",itemId);
                map.put("resName", resultTitle);
                map.put("resultTime", rTime);
                map.put("newNo", newNo);
                updateTodayResult(map);
            }
        });

    }

    private void updateTodayResult(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateTodayResult(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()){
                    Toast.makeText(UpdateTodayResultActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchTodayResult();

                    todayResultDialog.dismiss();
                }else {
                    Toast.makeText(UpdateTodayResultActivity.this, response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(UpdateTodayResultActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteData(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.deleteData(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchTodayResult();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(resultModelList,fromPosition,toPosition);
            Objects.requireNonNull(binding.todayResultRecyclerview.getAdapter()).notifyItemMoved(fromPosition,toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}