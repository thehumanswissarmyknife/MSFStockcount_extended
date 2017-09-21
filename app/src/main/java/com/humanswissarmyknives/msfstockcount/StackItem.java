package com.humanswissarmyknives.msfstockcount;

/**
 * Created by dennisvocke on 19.09.17.
 */

class StackItem {

    // needs basically an array for the items that still need to be pushed....

    Long timestamp;
    Batch batch;
    CountedItem countedItem;
    String type;

    // some method to get the info from the item - or can we use the inherent methods for batches and countedItems?
    // do we need the distinction between  stack and stackitem

    public StackItem(Batch myBatch) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.batch = myBatch;
        this.countedItem = null;
        this.type = "batchonly";

    }

    public StackItem(Batch myBatch, CountedItem myCountedItem) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.batch = myBatch;
        this.countedItem = myCountedItem;
        this.type = "fullitem";
    }

    void setTimestamp(long myTimeStamp) {
        timestamp = myTimeStamp;
    }

    Long getTimestamp() {
        return timestamp;
    }

    Batch getBatch() {
        return batch;
    }

    CountedItem getCountedItem() {
        return countedItem;
    }

    String getType() {
        return type;
    }

}
