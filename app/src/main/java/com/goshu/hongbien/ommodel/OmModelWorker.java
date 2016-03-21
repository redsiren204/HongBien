package com.goshu.hongbien.ommodel;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper class to parse the model.
 */
public class OmModelWorker {
    private static final ObjectMapper sObjectMapper = new ObjectMapper();

    public OmModelRequest parseFromString(String input) throws IOException {
        return sObjectMapper.readValue(input, OmModelRequest.class);
    }

    public OmListOfModels parseListFromString(String input) throws IOException {
        return sObjectMapper.readValue(input, OmListOfModels.class);
    }
}
