/*
 * Testerra
 *
 * (C) 2020, Mike Reiche, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {autoinject} from "aurelia-framework";
import {DataLoader} from "./data-loader";
import {ClassStatistics, ExecutionStatistics} from "./statistic-models";
import {CacheService} from "t-systems-aurelia-components/src/services/cache-service";
import {data} from "./report-model";


@autoinject()
export class StatisticsGenerator {
    constructor(
        private _dataLoader: DataLoader,
        private _cacheService:CacheService
    ) {
        this._cacheService.setDefaultCacheTtl(120);
    }

    getExecutionStatistics(): Promise<ExecutionStatistics> {
        return this._cacheService.getForKeyWithLoadingFunction("executionStatistics", () => {
            return this._dataLoader.getExecutionAggregate().then(executionAggregate => {

                const executionStatistics = new ExecutionStatistics();
                executionStatistics.setExecutionAggregate(executionAggregate);

                const loadingPromises = [];
                executionAggregate.testContexts.forEach(testContext => {
                    testContext.classContextIds.forEach(classContextId => {
                        const loadingPromise = this._dataLoader.getClassContextAggregate(classContextId).then(classContextAggregate => {
                            let classStatistics:ClassStatistics;
                            if (classContextAggregate.classContext.testContextName) {
                                classStatistics = executionStatistics.classStatistics.find(value => value.classAggregate.classContext.testContextName == classContextAggregate.classContext.testContextName);
                            }
                            if (!classStatistics) {
                                classStatistics = new ClassStatistics();
                                executionStatistics.addClassStatistics(classStatistics);
                            }
                            classStatistics.addClassAggregate(classContextAggregate);
                        });

                        loadingPromises.push(loadingPromise);
                    })
                });

                return Promise.all(loadingPromises).then(() => {
                    executionStatistics.updateStatistics();
                    return Promise.resolve(executionStatistics);
                })
            });
        })
    }
}

