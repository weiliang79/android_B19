package com.example.android_b19.ui.feed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_b19.R;
import com.example.android_b19.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFragment extends Fragment implements CategoryAdapter.ClickHandler {

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private TextView tvEmptyCategory;
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("Feed Category");
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Feeds").child(auth.getUid()).child("Category");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmptyCategory = view.findViewById(R.id.tv_category_empty);
        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        categoryAdapter = new CategoryAdapter(requireContext(), this);
        rvCategories.setAdapter(categoryAdapter);
        loadData();
    }

    private void loadData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.e("children count", "" + snapshot.getChildrenCount());
                if(snapshot.getChildrenCount() == 0){
                    tvEmptyCategory.setVisibility(View.VISIBLE);
                    List<Category> categoryList = new ArrayList<>();
                    categoryAdapter.setCategoryList(categoryList);
                } else {
                    tvEmptyCategory.setVisibility(View.GONE);
                    List<Category> categoryList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categoryList.add(new Category(UUID.fromString(dataSnapshot.getKey()), dataSnapshot.child("name").getValue(String.class)));
                    }
                    categoryAdapter.setCategoryList(categoryList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Cancel Loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.category_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_add_category){

            AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
            View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.dialog_add_category, null);

            EditText categoryName = dialogView.findViewById(R.id.et_category_name);
            TextView cancelBtn = dialogView.findViewById(R.id.btn_cancel_add_category);
            TextView saveBtn = dialogView.findViewById(R.id.btn_save_add_category);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = categoryName.getText().toString();

                    if(TextUtils.isEmpty(name)){
                        Toast.makeText(requireContext(), R.string.category_name_empty_toast, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Category category = new Category();
                    category.setName(name);
                    reference.child(UUID.randomUUID().toString()).setValue(category);

                    dialog.dismiss();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setView(dialogView);
            dialog.show();

            return true;
        } else if(item.getItemId() == R.id.menu_show_manage_category) {
            categoryAdapter.reverseHiddenManage();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void editCategory(UUID categoryId, String name){
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);

        TextView tvTitle = dialogView.findViewById(R.id.tv_title_add_category);
        tvTitle.setText(getResources().getText(R.string.edit_category_title));

        EditText etName = dialogView.findViewById(R.id.et_category_name);
        etName.setText(name);
        TextView tvCancel = dialogView.findViewById(R.id.btn_cancel_add_category);
        TextView tvSave = dialogView.findViewById(R.id.btn_save_add_category);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(categoryId.toString()).child("name").setValue(etName.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }

    @Override
    public void deleteCategory(UUID categoryId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder
                .setTitle(getResources().getString(R.string.delete_warning_title))
                .setMessage(getResources().getString(R.string.delete_warning_msg))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child(categoryId.toString()).removeValue();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();

        dialog.show();


    }
}
