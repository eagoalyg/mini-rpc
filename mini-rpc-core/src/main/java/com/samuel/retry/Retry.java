package com.samuel.retry;

import com.samuel.model.RpcResponse;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Retry {

    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
