package me.winds.album.event;


/**
 * Created by donglua on 15/6/30.
 */
public interface Selectable {


  /**
   * Indicates if the item at position position is selected
   *
   * @param path Photo of the item to check
   * @return true if the item is selected, false otherwise
   */
  boolean isSelected(String path);

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param path Photo of the item to toggle the selection status for
   */
  void toggleSelection(String path);

  /**
   * Clear the selection status for all items
   */
  void clearSelection();

  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  int getSelectedItemCount();




}
