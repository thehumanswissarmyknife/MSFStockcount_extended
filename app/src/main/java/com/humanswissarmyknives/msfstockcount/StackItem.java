package com.humanswissarmyknives.msfstockcount;

/**
 * Created by dennisvocke on 19.09.17.
 */

class StackItem {

    // needs basically an array for the items that still need to be pushed....

    int timestamp;
    Batch thisBatch;
    CountedItem thisCountedItem;

    // some method to get the info from the item - or can we use the inherent methods for batches and countedItems?
    // do we need the distinction between  stack and stackitem

    String password;

}
