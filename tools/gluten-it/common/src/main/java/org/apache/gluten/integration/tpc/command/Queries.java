/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gluten.integration.tpc.command;

import org.apache.gluten.integration.tpc.TpcMixin;
import org.apache.commons.lang3.ArrayUtils;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "queries", mixinStandardHelpOptions = true,
    showDefaultValues = true,
    description = "Run queries.")
public class Queries implements Callable<Integer> {
  @CommandLine.Mixin
  private TpcMixin mixin;

  @CommandLine.Mixin
  private DataGenMixin dataGenMixin;

  @CommandLine.Mixin
  private QueriesMixin queriesMixin;

  @CommandLine.Option(names = {"--random-kill-tasks"}, description = "Every single task will get killed and retried after running for some time", defaultValue = "false")
  private boolean randomKillTasks;

  @Override
  public Integer call() throws Exception {
    org.apache.gluten.integration.tpc.action.Queries queries =
        new org.apache.gluten.integration.tpc.action.Queries(dataGenMixin.getScale(), queriesMixin.queries(),
            queriesMixin.explain(), queriesMixin.iterations(), randomKillTasks);
    return mixin.runActions(ArrayUtils.addAll(dataGenMixin.makeActions(), queries));
  }
}
