package me.winds.album.adapter;

import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import me.winds.album.event.Selectable;


public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();

    protected List<String> selectedList;
    protected List<String> list;

    public SelectableAdapter() {
        selectedList = new ArrayList<>();
        list = new ArrayList<>();
    }

    /**
     * Indicates if the item at position where is selected
     *
     * @param path Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(String path) {
        return getSelectedList().contains(path);
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param path Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(String path) {
        if (selectedList.contains(path)) {
            selectedList.remove(path);
        } else {
            selectedList.add(path);
        }
    }

    public void deleteSelectedList() {
        list.removeAll(selectedList);
        selectedList.clear();
        notifyDataSetChanged();
    }

    private void removeItem() {

    }

    public void addItem(String path, int position) {
        list.add(position, path);
        notifyItemInserted(position);
    }

    public void addItem(String path) {
        int position = list.size();
        list.add(path);
        notifyItemInserted(position);
    }

    public void addList(List<String> mList) {
        list.addAll(mList);
        notifyDataSetChanged();
    }

    public void setList(List<String> mList) {
        list.clear();
        list.addAll(mList);
        notifyDataSetChanged();
    }

    public List getList() {
        return list;
    }

    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectedList.clear();
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedList.size();
    }


    public List<String> getSelectedList() {
        return selectedList;
    }

}