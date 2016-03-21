package com.goshu.hongbien.igmodel;

import java.io.IOException;

/**
 * Helper class to parse the model.
 */
public class IgModelWorker {
    public IgModelRequest parseFromString(String input) throws IOException {
        return IgModelRequest__JsonHelper.parseFromJson(input);
    }

    public IgListOfModels parseListFromString(String input) throws IOException {
        return IgListOfModels__JsonHelper.parseFromJson(input);
    }
}
