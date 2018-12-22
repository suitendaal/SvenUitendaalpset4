package com.example.svenu.svenuitendaalpset4;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


@SuppressLint("ValidFragment")
public class OptionFragment extends DialogFragment implements View.OnClickListener {

    Context context;
    TodoDatabase db;
    TodoAdapter adapter;

    public OptionFragment(TodoAdapter todoAdapter) {
        super();
        adapter = todoAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_option, container, false);

        context = getActivity().getApplicationContext();
        db = TodoDatabase.getInstance(context.getApplicationContext());

        Button deleteButton = rootView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);

        Button copyButton = rootView.findViewById(R.id.copy_button);
        copyButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete_button:
                db.deleteSelected();
                getDialog().dismiss();
                Cursor cursor = db.selectAll();
                adapter.swapCursor(cursor);
                break;
            case R.id.copy_button:
                db.copySelected();
                getDialog().dismiss();
                break;
        }
    }
}