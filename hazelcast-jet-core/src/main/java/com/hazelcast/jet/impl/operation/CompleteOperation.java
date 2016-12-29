/*
 * Copyright (c) 2008-2016, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.impl.operation;

import com.hazelcast.jet.impl.EngineContext;
import com.hazelcast.jet.impl.JetService;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;

class CompleteOperation extends EngineOperation {

    private long executionId;
    private Throwable error;

    CompleteOperation(String engineName, long executionId, Throwable error) {
        super(engineName);
        this.executionId = executionId;
        this.error = error;
    }

    private CompleteOperation() {
        // for deserialization
    }

    @Override
    public void run() throws Exception {
        JetService service = getService();
        EngineContext engineContext = service.getEngineContext(engineName);
        engineContext.completeExecution(executionId, error);
    }


    @Override
    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeLong(executionId);
        out.writeObject(error);
    }

    @Override
    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        executionId = in.readLong();
        error = in.readObject();
    }
}