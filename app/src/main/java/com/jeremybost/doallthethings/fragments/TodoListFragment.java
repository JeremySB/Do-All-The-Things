package com.jeremybost.doallthethings.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeremybost.doallthethings.CreateTodoItemActivity;
import com.jeremybost.doallthethings.R;
import com.jeremybost.doallthethings.TodoItemRecyclerViewAdapter;
import com.jeremybost.doallthethings.TodoItemRepository;
import com.jeremybost.doallthethings.models.TodoItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TodoListFragment extends Fragment implements
        TodoItemRepository.OnChangeListener {

    private OnListFragmentInteractionListener mListener;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TodoItemRecyclerViewAdapter adapter;
    private final List<TodoItem> items = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;

    private boolean completedMode = false;
    private static final String COMPLETED_MODE_ARG = "completed-mode-arg";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodoListFragment() {
    }

    public static TodoListFragment newInstance(boolean completedMode) {
        TodoListFragment fragment = new TodoListFragment();
        Bundle args = new Bundle();
        args.putBoolean(COMPLETED_MODE_ARG, completedMode);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            completedMode = getArguments().getBoolean(COMPLETED_MODE_ARG, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        refreshItems();
        adapter = new TodoItemRecyclerViewAdapter(items, mListener);
        recyclerView.setAdapter(adapter);

        fab = view.findViewById(R.id.todoFAB);
        fab.setOnClickListener(v -> addItem());

        coordinatorLayout = view.findViewById(R.id.todoListContainer);

        if(!completedMode) {

            TodoItemRepository.getInstance().setOnChangeListener(this);

            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback
                    (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    final TodoItem item = adapter.getItemAt(position);
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Item Completed!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", view1 -> {
                        // undo is selected, restore the deleted item
                        item.setCompleted(false);
                        items.add(position, item);
                        adapter.notifyItemInserted(position);
                    });

                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();

                    item.setCompleted(true);
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    items.remove(position);
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnTodoItemsChanged() {
        refreshItems();
        adapter.notifyDataSetChanged();
    }

    private void refreshItems() {
        items.clear();
        items.addAll(TodoItemRepository.getInstance().getItems(completedMode));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(TodoItem item);
    }

    private void addItem() {
        startActivity(new Intent(getActivity(), CreateTodoItemActivity.class));
    }
}
