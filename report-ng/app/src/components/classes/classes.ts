import {DataLoader} from "../../services/data-loader";
import {StatusConverter} from "../../services/status-converter";
import {autoinject} from "aurelia-framework";
import {StatisticsGenerator} from "../../services/statistics-generator";
import {ExecutionStatistics} from "../../services/statistic-models";
import {AbstractViewModel} from "../abstract-view-model";
import {data} from "../../services/report-model";
import {NavigationInstruction, RouteConfig} from "aurelia-router";
import IMethodContext = data.IMethodContext;
import IClassContext = data.IClassContext;
import MethodType = data.MethodType;

interface MethodAggregate {
    methodContext:IMethodContext,
    classContext:IClassContext
    failureAspect?:string,
}

@autoinject()
export class Classes extends AbstractViewModel {

    private _executionStatistics: ExecutionStatistics;
    private _selectedClass:string;
    private _selectedStatus:number;
    private _availableStatuses:number[];
    private _filteredMethodAggregates:MethodAggregate[];
    private _configurationMethods:boolean = true;
    private _searchQuery:string;
    private _searchRegexp:RegExp;

    constructor(
        private _dataLoader: DataLoader,
        private _statusConverter: StatusConverter,
        private _statisticsGenerator: StatisticsGenerator,
    ) {
        super();
    }

    activate(
        params: any,
        routeConfig: RouteConfig,
        navInstruction: NavigationInstruction
    ) {
        super.activate(params, routeConfig, navInstruction);
        if (params.q) {
            this._searchQuery = params.q;
        }
        if (params.class) {
            this._selectedClass = params.class;
        }
        if (params.status) {
            this._selectedStatus = this._statusConverter.getStatusForClass(params.status);
        }

        if (params.configurationMethods) {
            if (params.configurationMethods.toLowerCase() == "true") {
                this._configurationMethods = true;
            } else {
                this._configurationMethods = false;
            }
        }
    }

    attached() {
        this._statisticsGenerator.getExecutionStatistics().then(executionStatistics => {
            this._availableStatuses = this._statusConverter.relevantStatuses;
            this._executionStatistics = executionStatistics;
            this._filter();
        });
    }

    private _filter() {
        const queryParams:any = {};
        if (this._searchQuery?.length > 0) {
            this._searchRegexp = new RegExp("(" + this._searchQuery + ")", "ig");
            queryParams.q = this._searchQuery;
        } else {
            this._searchRegexp = null;
        }

        if (this._selectedClass) {
            queryParams.class = this._selectedClass;
        }

        if (this._selectedStatus >= 0) {
            queryParams.status = this._statusConverter.getClassForStatus(this._selectedStatus);
        }

        queryParams.configurationMethods = this._configurationMethods;

        console.log("filter", queryParams);
        this.updateUrl(queryParams);

        this._filteredMethodAggregates = [];
        this._executionStatistics.classStatistics
            .filter(classStatistic => {
                return (!this._selectedClass || classStatistic.classAggregate.classContext.simpleClassName == this._selectedClass)
            })
            .forEach(classStatistic => {
                classStatistic.classAggregate.methodContexts
                    .filter(methodContext => {
                        return (!this._selectedStatus || methodContext.contextValues.resultStatus == this._selectedStatus)
                    })
                    .filter(methodContext => {
                        return (this._configurationMethods == true || (this._configurationMethods == false && methodContext.methodType == MethodType.TEST_METHOD))
                    })
                    .map(methodContext => {
                        return {
                            classContext: classStatistic.classAggregate.classContext,
                            failureAspect: (this._statusConverter.failedStatuses.indexOf(methodContext.contextValues.resultStatus)>=0?methodContext.errorContext?.description||methodContext.errorContext?.stackTrace?.cause.className + (methodContext.errorContext?.stackTrace?.cause?.message?": "+methodContext.errorContext?.stackTrace?.cause?.message.trim().replace("\n", "<br/>"):""):null),
                            methodContext: methodContext
                        }
                    })
                    .filter(methodAggregate => {
                        return (!this._searchRegexp || (methodAggregate.failureAspect?.match(this._searchRegexp) || methodAggregate.methodContext.contextValues.name.match(this._searchRegexp)));
                    })
                    .forEach(methodAggregate => {
                        this._filteredMethodAggregates.push(methodAggregate);
                    })
            })
    }
}

