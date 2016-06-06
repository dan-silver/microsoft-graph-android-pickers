package com.microsoftgraph.graphpickers;

import com.microsoft.graph.extensions.IGraphServiceClient;

/**
 * Created by dan on 6/4/16.
 */
public class GraphPickerLib {
    public static String TAG = "GraphPickerLib";

    private static IGraphServiceClient mClient;
    public static void init(IGraphServiceClient client) {
        GraphPickerLib.mClient = client;
    }
    public static IGraphServiceClient getClient() {
        return mClient;
    }
}
