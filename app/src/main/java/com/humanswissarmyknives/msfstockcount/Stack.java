package com.humanswissarmyknives.msfstockcount;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dennisvocke on 19.09.17.
 */

public class Stack extends Application {
    ArrayList<StackItem> myStack;

    // variable to store the currentStackItem
    StackItem currentStackItem;

    public Stack() {
        this.myStack = new ArrayList<>();

    }

    StackItem getOldestStackItem() {
        if (myStack.size() > 0) {
            return myStack.get(0);
        }
        return null;
    }

    void removeOldestStackItem() {
        myStack.remove(0);
    }

    int getStackHeight() {
        return myStack.size();
    }

    int addStackItem(StackItem newStackItem) {

        myStack.add(newStackItem);
        return myStack.size();
    }

    Long getStackAge() {
        return myStack.get(0).getTimestamp();
    }

    int pushStackItemsToServer() {

        // do we have to chekc the reachability of the server?

        // check if the stack is empty return 0
        if (myStack.size() == 0) {
            return 0;
        }

        // while items are int he stack try to push them to the server

        if (myStack.size() > 0) {
            currentStackItem = myStack.get(0);

            PostJson pushItem;
            // check if the stackItem has one or two attributes (batch or bacht & countedItem)
            if (currentStackItem.getType() == "batchonly") {
                Log.i("trying to push", "batch only");
                pushItem = new PostJson(currentStackItem.getBatch());
                pushItem.execute();
                Log.i("Status:", pushItem.getStatus().toString());


            } else if (currentStackItem.getType() == "fullitem") {
                Log.i("trying to push", "full item");
                pushItem = new PostJson(currentStackItem.getBatch(), currentStackItem.getCountedItem());
                pushItem.execute();
                Log.i("Status:", pushItem.getStatus().toString());
            }
        }

        // return the number of items remaining in the stack
        return myStack.size();

    }

    // some method to push the stackitem on position 0 to the db.
    // check db connection
}
