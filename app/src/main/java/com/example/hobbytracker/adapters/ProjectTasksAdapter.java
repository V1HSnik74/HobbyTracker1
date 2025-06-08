package com.example.hobbytracker.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hobbytracker.R;
import com.example.hobbytracker.data.model.Task;
import com.example.hobbytracker.listeners.OnTaskChangedListener;
import com.example.hobbytracker.managers.AchievementsManager;

import java.util.List;

// Shows all tasks for a single project.
public class ProjectTasksAdapter extends RecyclerView.Adapter<ProjectTasksAdapter.TaskViewHolder> {
    private List<Task> tasks;
    private final OnTaskChangedListener taskChangedListener;
    private final Context context;

    public ProjectTasksAdapter(List<Task> tasks, OnTaskChangedListener taskChangedListener, Context context) {
        this.tasks = tasks;
        this.taskChangedListener = taskChangedListener;
        this.context = context;
    }

    public void setTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.checkBox.setText(task.text);
        holder.checkBox.setChecked(task.isDone);

        if (task.isDone) {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkBox.setPaintFlags(holder.checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.isDone = isChecked;
            if (taskChangedListener != null) taskChangedListener.onTaskCheckedChanged(task);
            AchievementsManager.getInstance(context).updateStatistics();
        });

        ImageView deleteButton = holder.itemView.findViewById(R.id.deleteProjectTask);
        deleteButton.setOnClickListener(v -> {
            if (taskChangedListener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                taskChangedListener.deleteProjectTask(tasks.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        TaskViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.projectTaskCheckBox);
        }
    }
}