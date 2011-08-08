/**
 *    Copyright 2011 Alexander Pucher
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.alexpucher.batchcompare.task;

import com.alexpucher.batchcompare.Processor;

/**
 * Task wrapper for arbitrary {@link Processor}.
 * 
 * @author Alexander Pucher
 * 
 */
public class ProcessorTask extends AbstractTask {

    private Processor processor;

    /**
     * Create {@link ProcessorTask} instance with given {@link Processor}.
     * 
     * @param processor
     *            processor instance
     */
    public ProcessorTask(Processor processor) {
        super();
        this.processor = processor;
    }

    @Override
    public void executeImpl() {
        this.data = this.processor.execute(this.data);
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

}
