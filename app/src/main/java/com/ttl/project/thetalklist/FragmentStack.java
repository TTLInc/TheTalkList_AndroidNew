package com.ttl.project.thetalklist;

import android.support.v4.app.Fragment;

import java.util.Stack;

/**
 * Created by Saubhagyam on 12/04/2017.
 */

//To have to stack of the fragments
public class FragmentStack extends Stack<Fragment>{


    private static final FragmentStack ourInstance = new FragmentStack();

    public static FragmentStack getInstance() {
        return ourInstance;
    }

    private FragmentStack() {
    }

}
