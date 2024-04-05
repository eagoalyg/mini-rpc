package com.samuel.retry;

import com.samuel.model.RpcResponse;

import java.io.IOException;
import java.util.concurrent.Callable;

public class NoRetry implements Retry{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
